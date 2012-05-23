import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

  val appName = "lmsplay"
  val appVersion = "1.0"

  resolvers += "repo.novus rels" at "http://repo.novus.com/releases/"

  resolvers += "repo.novus snaps" at "http://repo.novus.com/snapshots/"

  val appDependencies = Seq(
    // Add your project dependencies here,
    "org.codehaus.jackson" % "jackson-core-asl" % "1.9.2",
    "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.2",
    "com.fasterxml" % "jackson-module-scala" % "1.9.1",
    "org.scalatest" %% "scalatest" % "1.7.2" % "test",
    "org.apache.httpcomponents" % "httpclient" % "4.1.3",
    "org.ektorp" % "org.ektorp" % "1.2.1",
    "com.mongodb.casbah" %% "casbah" % "2.1.5-1",
    "com.novus" %% "salat-core" % "0.0.8-SNAPSHOT"
  )

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    // Add your own project settings here
  )

}
