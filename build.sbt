name := """blog"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "com.google.guava" % "guava" % "18.0"
)

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")