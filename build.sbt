name := """WebsiteBuilder"""

version := "1.0.1-Beta"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  cache,
	jdbc,
	anorm,
	"org.postgresql" % "postgresql" % "9.3-1100-jdbc4",
	"org.reactivemongo" %% "play2-reactivemongo" % "0.10.2"
)