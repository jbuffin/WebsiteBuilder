import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

	val appName = "ChurchSite"
	val appVersion = "1.0-SNAPSHOT"

	val appDependencies = Seq(
		jdbc,
		anorm
	)

	val main = play.Project(appName, appVersion, appDependencies).settings(      
	)

}
