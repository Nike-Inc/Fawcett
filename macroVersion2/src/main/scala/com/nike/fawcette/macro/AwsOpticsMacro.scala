package com.nike.fawcett.opticsMacro


import cats.data.NonEmptyList
import cats.implicits._
import scala.reflect.macros.whitebox.Context
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation
import scala.annotation.compileTimeOnly

/* Copyright 2019-present, Nike, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-3-Clause license found in
 * the LICENSE file in the root directory of this source tree.
 */

@compileTimeOnly("enable macro paradise to expand macro annotations")
class awsOptics(className: String, exclude: List[String] = Nil) extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro AwsOpticsMacro.genOptics
}

// $COVERAGE-OFF$
object AwsOpticsMacro {
  case class Arguments(className: String, excludes: List[String])

  class ParseArguments(val c: Context) extends Compatibility {
    import c.universe._

    def arguments = c.macroApplication match {
      case Apply(Select(Apply(Select(New(Ident(_)), t), args), _), _) if t == termNames.CONSTRUCTOR =>
        args match {
          case List(Literal(Constant(className: String))) => Arguments(className, List.empty)

          case List(Literal(Constant(className: String)),
          Apply(Ident(TermName("List")), values: List[Tree])) =>
            Arguments(className, parseArgList(values))

          case List(
          Literal(Constant(className: String)),
          NamedArg(Ident(TermName("exclude")), Apply(Ident(TermName("List")), values: List[Tree]))) =>

            Arguments(className, parseArgList(values))

          case _ => c.abort(
            c.enclosingPosition,
            "You must provide a class to @awsOptics")
        }
      case _ => c.abort(
        c.enclosingPosition,
        "You must provide a class to @awsOptics")
    }

    def parseArgList(args: List[Tree]): List[String] = args.foldLeft(List.empty[String]) {
      case (acc, Literal(Constant(value: String))) => acc :+ value }
  }
  // $COVERAGE-ON$

  def genOptics(c: Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    def camelCase(string: String) = {
      val regex = "(^[a-z]+|[A-Z0-9]+(?![a-z])|[A-Z][a-z]+)".r
      (regex.findAllIn(string).toList match {
        case head :: tail => head.toLowerCase :: tail.map(value => value.substring(0, 1).toUpperCase + value.substring(1))
        case Nil => c.abort(c.enclosingPosition, s"Unabled to convert '$string' to proper camelCase value")
      }).mkString
    }

    val arguments = new ParseArguments(c).arguments

    val fullClassName = s"software.amazon.awssdk.services.${arguments.className}"
    val sourceClass = Class.forName(fullClassName)
    val sourceType = c.mirror.staticClass(fullClassName).toType
    val sourceTypeBuilder = c.mirror.staticClass(s"$fullClassName.Builder").toType
    val classMethods = sourceClass.getMethods.toList

    val classExcludes =
      sourceClass.getSuperclass.getMethods.toList.map(_.getName).toSet ++
      Set("serializableBuilderClass", "copy", "builder", "toBuilder", "sdkFields", "getValueForField",
        "equalsBySdkFields")

    val allExcludes = (arguments.excludes ++ classExcludes.toList).map(_.toLowerCase)

    val methods = (classMethods.map(_.getName))
      .filterNot(method => allExcludes.contains(method.toLowerCase))


    def createLens(valueName: String): (Boolean, Tree) = {
      val defaultGetFunction = TermName(valueName)

      val aType = sourceType.decl(defaultGetFunction).asMethod.returnType

      val defaultSetFunction = q"(a: $aType) => (s: $sourceType) => s.toBuilder.${TermName(valueName)}(a).build()"

      val (returnType, getFunction: Tree, setFunction: Tree, needsConversion) = aType match {
        case x if x =:= typeOf[java.lang.String] =>
          (tq"String", q"(s: $sourceType) => s.$defaultGetFunction", defaultSetFunction, false)

        case x if x =:= typeOf[java.lang.Boolean] =>
          (tq"Boolean", q"(s: $sourceType) => s.$defaultGetFunction", q"(a: Boolean) => $defaultSetFunction(a)", false)

        case x if x =:= typeOf[java.lang.Integer] =>
          (tq"Int", q"(s: $sourceType) => s.$defaultGetFunction", q"(a: Int) => $defaultSetFunction(a)", false)

        case x if x <:< typeOf[java.util.Map[_, _]] =>
          val List(key, value) = x.typeArgs
          val mapType = tq"Map[$key, $value]"
          (mapType, q"(s: $sourceType) => s.$defaultGetFunction.asScala.toMap", q"(a: $mapType) => $defaultSetFunction(a.asJava)", true)

        case x if x <:< typeOf[java.util.List[_]] =>
          val List(tpe) = x.typeArgs
          val listType = tq"List[$tpe]"
          (listType, q"(s: $sourceType) => s.$defaultGetFunction.asScala.toList", q"(a: $listType) => $defaultSetFunction(a.asJava)", true)

        case x => (tq"$x", q"(s: $sourceType) => s.$defaultGetFunction", defaultSetFunction, false)
      }

      val lensName = TermName(camelCase(valueName))

      (needsConversion,
      q"""
        ${Modifiers()} val $lensName: monocle.Lens[$sourceType, $returnType] =
          monocle.Lens[$sourceType, $returnType]($getFunction)($setFunction)
      """)
    }

    // Starting with AWS 2.15.17 composition fails due to an internal representation which hides a null value on get.
    // Using has* to try and recover this isn't an option because `f compose g` ends up not being equal to
    // Lens(g)(Lens(f)(s)) when the order of empty transformations results in what should be null becoming empty
    // map/list
    // This means no map/list set and empty map/list aren't equal.
    // This definition of equality is effectively equal not technically equal.
    def createEquality(methods: NonEmptyList[String]): Tree = {
      val comparisions = methods.map({ valueName =>
        q"a1.${TermName(valueName)} == a2.${TermName(valueName)}"
      }).reduceLeft((acc, t) => q"$acc && $t")

      q"""
      cats.Eq.instance { case (a1, a2) =>
        $comparisions
      }
      """
    }

    val (conversionStates: List[Boolean], lenses: List[Tree]) = methods.toList.map(method => createLens(method)).unzip

    val equality = createEquality(NonEmptyList.fromListUnsafe(methods))

    val imports: List[Tree] = if (conversionStates.reduce(_ || _) == true) {
      List(q"import scala.jdk.CollectionConverters._")
    } else List.empty

    val result = annottees.head.tree match {
      case q"""$mods trait $tpname[..$tparams] extends { ..$earlydefns } with ..$parents { $self => ..$statements }""" =>

        val Modifiers(flags, privateWithin, anns) = mods

        val flags1 = (flags match {
          case flags: Int => flags & ~scala.reflect.internal.Flags.INTERFACE
          case flags: Long => flags & ~scala.reflect.internal.Flags.INTERFACE
        }).toLong.asInstanceOf[FlagSet]

        val mods1 = Modifiers(flags1, privateWithin, anns)

        q"""
          $mods1 trait $tpname[..$tparams] extends { ..$earlydefns } with ..$parents { $self =>
            ..$imports

            implicit val ${TermName(c.freshName("eq"))}: cats.Eq[$sourceType] = $equality

            ..$lenses

            ..$statements
          }

          object ${tpname.toTermName} extends $tpname
        """
    }

    c.Expr[Any](result)
  }
}
