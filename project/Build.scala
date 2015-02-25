import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

	val appName = "WebsiteBuilder"
	val appVersion = "1.0.1-Beta"

	val appDependencies = Seq(
        cache,
		jdbc,
		anorm,
		"org.postgresql" % "postgresql" % "9.3-1100-jdbc4",
		"org.reactivemongo" %% "play2-reactivemongo" % "0.10.2"
	)

	val main = play.Project(appName, appVersion, appDependencies).settings()

}
