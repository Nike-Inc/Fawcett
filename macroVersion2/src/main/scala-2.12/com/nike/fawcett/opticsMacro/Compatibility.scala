package com.nike.fawcett.opticsMacro

import scala.reflect.macros.whitebox.Context

private[opticsMacro] trait Compatibility {
  val c: Context

  import c.universe._
  val NamedArg = AssignOrNamedArg
  type NamedArg = AssignOrNamedArg
}
