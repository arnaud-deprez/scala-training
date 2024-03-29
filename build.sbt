import sbt._
import training.{Dependencies, Settings}

name := "scala-training"

lazy val root = project.in(file("."))
	.settings(Settings.commonSettings)
  .aggregate(forTheImpatient, reactivePrograming, akkaCourse)

lazy val forTheImpatient = project.in(file("for-the-impatient"))
	.settings(Settings.commonSettings)
	.settings(Dependencies.forTheImpatient)

lazy val reactivePrograming = project.in(file("reactive-programing"))
	.settings(Settings.commonSettings)
	.settings(Dependencies.reactivePrograming)

lazy val akkaCourse = project.in(file("akka-course"))
	.settings(Settings.commonSettings)
	.settings(Dependencies.akkaCourse)