import sbt._
import training.{Dependencies, Settings}

name := "scala-training"

lazy val root = project.in(file("."))
	.settings(Settings.commonSettings)

lazy val forTheImpatient = project.in(file("for-the-impatient"))
	.settings(Settings.commonSettings)
	.settings(Dependencies.forTheImpatient)

lazy val circuit = project.in(file("circuit"))
	.settings(Settings.commonSettings)
	.settings(Dependencies.circuit)