import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

	val appName = "ChurchSite"
	val appVersion = "0.0.2-SNAPSHOT"

	val appDependencies = Seq(
		jdbc,
		anorm,
		"postgresql" % "postgresql" % "8.4-702.jdbc4",
		"org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
		"mysql" % "mysql-connector-java" % "5.1.21"
	)

	val main = play.Project(appName, appVersion, appDependencies).settings()

}
