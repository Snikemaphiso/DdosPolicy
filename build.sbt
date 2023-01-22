name := "DdosPolicy"

version := "0.1"

scalaVersion := "2.13.8"

lazy val akkaVersion = "2.7.0"
lazy val akkaHttpVersion = "10.4.0"

//fork := true

val mainDependencies = Seq(
  "com.typesafe.play" %% "play-json" % "2.9.2",
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.scalameta" %% "scalameta" % "4.7.2"
)

val testDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.1.0" % Test
)

libraryDependencies ++= mainDependencies ++ testDependencies

ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case PathList("reference.conf") =>
    val log = sLog.value
    log.info("concatinating reference.conf")
    MergeStrategy.concat
  case "application.conf"       => MergeStrategy.concat
  case _                        => MergeStrategy.first
}