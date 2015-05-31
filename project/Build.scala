import sbt._
import Keys._
import Dependencies._

object MyBuild extends Build {

	lazy val commonSettings = Seq(
		organization := "be.arndep.scala",
		version := "1.0.0-SNAPSHOT",
		scalaVersion := "2.11.6"
	)

	lazy val root = project.in(file("."))
		.settings(commonSettings)
		.aggregate(forTheImpatient)

	lazy val forTheImpatient = project.in(file("for-the-impatient"))
		.settings(commonSettings)
		.settings(libraryDependencies ++= forTheImpatientDependencies)
}