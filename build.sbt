name := "DdosPolicy"

version := "0.1"

scalaVersion := "2.13.8"

lazy val akkaVersion = "2.6.19"

//fork := true

val mainDependencies = Seq(
  "com.typesafe.play" %% "play-json" % "2.9.2",
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.scalameta" %% "scalameta" % "4.7.2"
)

val testDependencies = Seq(
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.1.0" % Test
)

libraryDependencies ++= mainDependencies ++ testDependencies