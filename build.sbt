name in ThisBuild := "iconsole"
organization in ThisBuild := "com.outr"
version in ThisBuild := "1.0.1"
scalaVersion in ThisBuild := "2.12.4"
scalacOptions in ThisBuild += "-feature"
resolvers in ThisBuild += Resolver.sonatypeRepo("releases")

val youi = "0.8.1"

lazy val core = crossProject.in(file("core"))
  .settings(
    name := "iconsole-core",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % scalaVersion.value,
      "io.youi" %%% "youi-app" % youi
    )
  )

lazy val coreJS = core.js
lazy val coreJVM = core.jvm

lazy val iconsole = crossProject.in(file("console")).dependsOn(core)
  .settings(
    name := "iconsole"
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