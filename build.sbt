import Dependencies._

val currentScalaVersion = "2.12.8"
val scalaVersions = Seq("2.11.12", currentScalaVersion)
val checkEvictionsTask = taskKey[Unit]("Task that fails build if there are evictions")


val sharedScalacOptions = Seq(
  "-deprecation",
  "-feature",
  "-unchecked")

val releaseSettings = Seq(
  organization := "com.nike.fawcett",
  releaseCrossBuild := true,
  scalacOptions ++= sharedScalacOptions ++ Seq("-Xfatal-warnings", "-Xlint", "-Xlint:-adapted-args"),
  scalacOptions in (Compile,console) ++= sharedScalacOptions
)

val commonSettings = Seq(
  resolvers += Resolver.sonatypeRepo("releases"),
  scalaVersion := currentScalaVersion,
  crossScalaVersions := scalaVersions,
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  libraryDependencies ++= monocle,
  dependencyOverrides += catsCore,
  scalacOptions in (Compile, console) ~=
    (_ filterNot Set("-Xfatal-warnings", "-Xlint", "-Ywarn-unused-import")),
  coverageMinimum := 100,
  coverageFailOnMinimum := true,
  libraryDependencies += scalaTest % Test,
  libraryDependencies += scalaCheck % Test,
  libraryDependencies += pegDown % Test,
  libraryDependencies += discipline % Test,
  testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oDF"),
  testOptions in Test +=
    Tests.Argument(TestFrameworks.ScalaTest, "-h", "target/test-reports-html"),
  checkEvictionsTask := {
    if (evicted.value.reportedEvictions.nonEmpty) {
      throw new IllegalStateException("There are some incompatible classpath evictions warnings.")
    }
  })

lazy val root = (project in file("."))
  .aggregate(
    macroAwsVersion1, macroAwsVersion2,
    sqsVersion1, sqsVersion2)
  .settings(
    inThisBuild(List(
      organization := "com.nike",
      scalaVersion := currentScalaVersion
    )),
    name := "Fawcett",
  )
  .settings(commonSettings)
  .settings(
    addCommandAlias("check", "; +clean; checkEvictionsTask; coverage; +test; coverageReport"),
    licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
    description := "A collection of Monocle lenses for Amazon Web Services' Java models."
  )

lazy val macroAwsVersion1 = (project in file("./macroVersion1"))
  .settings(commonSettings)
  .settings(
    libraryDependencies += Dependencies.aws(Dependencies.Aws1, "core")
  )

lazy val macroAwsVersion2 = (project in file("./macroVersion2"))
  .settings(commonSettings)
  .settings(
    libraryDependencies += Dependencies.aws(Dependencies.Aws2, "core")
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
    "io.netty" % "netty-codec-http" % "4.1.32.Final",
    "io.netty" % "netty-handler" % "4.1.32.Final",
    "org.reactivestreams" % "reactive-streams" % "1.0.2",
  )
)

lazy val sqsVersion1 = (project in file("./sqs"))
  .dependsOn(macroAwsVersion1)
  .settings(commonSettings)
  .settings(commonAwsVersion1)
  .settings(releaseSettings)
  .settings(
    name := "fawcett-sqs-v1",
    libraryDependencies += Dependencies.aws(Dependencies.Aws1, "sqs")
  )

lazy val sqsVersion2 = (project in file("./sqs"))
  .dependsOn(macroAwsVersion2)
  .settings(commonAwsVersion2)
  .settings(commonSettings)
  .settings(releaseSettings)
  .settings(
    name := "fawcett-sqs-v2",
    target := file("target/v2"),
    libraryDependencies += Dependencies.aws(Dependencies.Aws2, "sqs")
  )
