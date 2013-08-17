import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

	val appName = "ChurchSite"
	val appVersion = "0.0.6"

	val appDependencies = Seq(
		jdbc,
		anorm,
		"postgresql" % "postgresql" % "9.1-901-1.jdbc4",
		"org.reactivemongo" %% "play2-reactivemongo" % "0.9"
	)

	val main = play.Project(appName, appVersion, appDependencies).settings()

}
