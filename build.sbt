name in ThisBuild := "iconsole"
organization in ThisBuild := "com.outr"
version in ThisBuild := "1.0.0-SNAPSHOT"
scalaVersion in ThisBuild := "2.12.3"

val youi = "0.8.0-SNAPSHOT"

lazy val iconsole = crossProject.in(file("."))
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "io.youi" %%% "youi-app" % youi
    )
  )

lazy val iconsoleJS = iconsole.js
lazy val iconsoleJVM = iconsole.jvm

lazy val example = crossApplication.in(file("example"))
  .settings(
    youiVersion := youi
  )
  .dependsOn(iconsole)

lazy val exampleJS = example.js
lazy val exampleJVM = example.jvm