name := """websitebuilder"""

version := "1.0.1-Beta"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  cache,
	jdbc,
	anorm,
	"org.postgresql" % "postgresql" % "9.3-1102-jdbc4",
	"org.reactivemongo" %% "play2-reactivemongo" % "0.10.5.akka23-SNAPSHOT"
)
