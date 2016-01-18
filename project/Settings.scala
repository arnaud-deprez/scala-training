package training

import sbt.Keys._

object Settings {
	lazy val commonSettings = Seq(
		organization := "be.arndep.scala",
		version := "1.0.0-SNAPSHOT",
		scalaVersion := "2.11.7"
	)
}