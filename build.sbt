import Dependencies._

val currentScalaVersion = "2.13.0"
val scalaVersions = Seq("2.12.10", currentScalaVersion)
val checkEvictionsTask = taskKey[Unit]("Task that fails build if there are evictions")


val sharedScalacOptions = Seq(
  "-deprecation",
  "-feature",
  "-unchecked")

val releaseSettings = Seq(
  organization := "com.nike.fawcett",
  organizationName := "Nike",
  organizationHomepage := Some(url("http://engineering.nike.com")),
  releaseCrossBuild := true,
  scalacOptions ++= sharedScalacOptions ++ Seq("-Xfatal-warnings", "-Xlint", "-Xlint:-adapted-args"),
  scalacOptions in (Compile,console) ++= sharedScalacOptions,
  bintrayOrganization := Some("nike"),
  bintrayPackageLabels := Seq("monocle", "aws"),
  licenses := Seq("BSD 3-Clause" -> url("https://opensource.org/licenses/BSD-3-Clause")),
  homepage := Some(url("https://github.com/Nike-Inc/fawcett")),
  startYear := Some(2019),
  scmInfo := Some(ScmInfo(
    url("https://github.com/Nike-Inc/fawcett"),
    "scm:git@github.com:Nike-Inc/fawcett.git"
  )),
  developers := List(
    Developer(
      id = "vendamere",
      name = "Peter Vendamere",
      email = "vendamere@gmail.com",
      url = url("https://github.com/vendamere")
    )
  ),
)

val commonSettings = Seq(
  resolvers += Resolver.sonatypeRepo("releases"),
  scalaVersion := currentScalaVersion,
  crossScalaVersions := scalaVersions,
  libraryDependencies ++= (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, major)) if major <= 12 => compilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full) :: Nil
    case _ => Nil
  }),
  libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value,
  libraryDependencies += "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided",
  libraryDependencies += scalaCollectionCompat,
  libraryDependencies ++= monocle,
  dependencyOverrides += catsCore,
  scalacOptions in (Compile, console) ~=
    (_ filterNot Set("-Xfatal-warnings", "-Xlint", "-Ywarn-unused-import")),
  scalacOptions ++= (CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, major)) if major >= 13 => "-Ymacro-annotations" :: Nil
    case _ => Nil
  }),
  coverageMinimum := 100,
  coverageFailOnMinimum := true,
  libraryDependencies += scalaCheck % Test,
  libraryDependencies += flexmark % Test,
  libraryDependencies += discipline % Test,
  testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF"),
  testOptions in Test +=
    Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports-html"),
  checkEvictionsTask := {
    if (evicted.value.reportedEvictions.nonEmpty) {
      throw new IllegalStateException("There are some incompatible classpath evictions warnings.")
    }
  }
)

lazy val root = (project in file("."))
  .aggregate(
    macroAwsVersion1, macroAwsVersion2,
    sqsVersion1, sqsVersion2)
  .settings(skip in publish := true)
  .settings(releaseSettings)
  .settings(commonSettings)
  .settings(
    addCommandAlias("check", "; +clean; checkEvictionsTask; coverage; +test; coverageReport"),
    licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
    description := "A collection of Monocle lenses for Amazon Web Services' Java models."
  )

lazy val macroAwsVersion1 = (project in file("./macroVersion1"))
  .settings(skip in publish := true)
  .settings(commonSettings)
  .settings(
    libraryDependencies += Dependencies.aws(Dependencies.Aws1, "core")
  )

lazy val macroAwsVersion2 = (project in file("./macroVersion2"))
  .settings(skip in publish := true)
  .settings(commonSettings)
  .settings(
    libraryDependencies += Dependencies.aws(Dependencies.Aws2, "core"),
  )

lazy val commonAwsVersion1 = Seq(
  target := file("target/v1"),
  unmanagedSourceDirectories in Compile += baseDirectory.value / "src/main/aws-version-1",
  unmanagedSourceDirectories in Test += baseDirectory.value / "src/test/aws-version-1"
)

lazy val commonAwsVersion2 = Seq(
  target := file("target/v2"),
  unmanagedSourceDirectories in Compile += baseDirectory.value / "src/main/aws-version-2",
  unmanagedSourceDirectories in Test += baseDirectory.value / "src/test/aws-version-2",
  dependencyOverrides ++= List(
    "io.netty" % "netty-codec-http" % "4.1.42.Final",
    "io.netty" % "netty-handler" % "4.1.42.Final",
    "org.reactivestreams" % "reactive-streams" % "1.0.2",
    "com.fasterxml.jackson.core" % "jackson-annotations" % "2.10.0",
  )
)

lazy val sqsVersion1 = (project in file("./sqs"))
  .dependsOn(macroAwsVersion1 % "compile-internal, test-internal")
  .settings(commonSettings)
  .settings(commonAwsVersion1)
  .settings(releaseSettings)
  .settings(
    name := "fawcett-sqs-v1",
    description := "Collection of Monocle lenses for AWS SQS version 1",
    bintrayPackageLabels += "sqs",
    libraryDependencies += Dependencies.aws(Dependencies.Aws1, "sqs"),
    mappings in (Compile, packageBin) ++= mappings.in(macroAwsVersion1, Compile, packageBin).value,
    mappings in (Compile, packageSrc) ++= mappings.in(macroAwsVersion1, Compile, packageSrc).value
  )

lazy val sqsVersion2 = (project in file("./sqs"))
  .dependsOn(macroAwsVersion2 % "compile-internal, test-internal")
  .settings(commonAwsVersion2)
  .settings(commonSettings)
  .settings(releaseSettings)
  .settings(
    name := "fawcett-sqs-v2",
    description := "Collection of Monocle lenses for AWS SQS version 2",
    bintrayPackageLabels += "sqs",
    libraryDependencies += Dependencies.aws(Dependencies.Aws2, "sqs"),
    mappings in (Compile, packageBin) ++= mappings.in(macroAwsVersion2, Compile, packageBin).value,
    mappings in (Compile, packageSrc) ++= mappings.in(macroAwsVersion2, Compile, packageSrc).value
  )
