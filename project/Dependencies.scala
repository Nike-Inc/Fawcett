import sbt._

object Dependencies {
  sealed trait AwsVersions {
    val namespace: String
    val version: String
  }

  object Aws1 extends AwsVersions {
    val namespace = "com.amazonaws"
    val version = "1.11.899"
  }
  object Aws2 extends AwsVersions {
    val namespace = "software.amazon.awssdk"
    val version = "2.15.29"
  }

  def aws(awsVersion: AwsVersions, service: String) = {
    val packageName = awsVersion match {
      case Aws1 => s"aws-java-sdk-$service"
      case Aws2 => service
    }

    awsVersion.namespace % packageName % awsVersion.version exclude("commons-logging", "commons-logging")
  }

  val monocleVersion = "2.0.3"
  val monocle = Seq(
    "com.github.julien-truffaut"  %%  "monocle-core" % monocleVersion,
    "com.github.julien-truffaut" %%  "monocle-law" % monocleVersion % "test"
  )
  val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.14.1"
  val catsCore = "org.typelevel" %% "cats-core" % "2.2.0"
  val flexmark = "com.vladsch.flexmark" % "flexmark-all" % "0.35.10"
  val discipline = Seq(
    "org.typelevel" %% "discipline-core" % "1.1.2" % Test,
    "org.typelevel" %% "discipline-scalatest" % "2.0.0" % Test
  )
  val scalaCollectionCompat = "org.scala-lang.modules" %% "scala-collection-compat" % "2.2.0"
}
