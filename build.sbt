name in ThisBuild := "iconsole"
organization in ThisBuild := "com.outr"
version in ThisBuild := "1.0.0-SNAPSHOT"
scalaVersion in ThisBuild := "2.12.2"

val youi = "0.5.1-SNAPSHOT"

lazy val iconsole = crossProject.in(file("."))
  .settings(
    libraryDependencies ++= Seq(
      "io.youi" %%% "youi-app" % youi
    )
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      "com.outr" %% "scarango-driver" % "0.7.0"
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