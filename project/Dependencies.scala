package training

import sbt.Keys._
import sbt._

object Dependencies {
	/**
	 * Libraries
	 */
	val junit = "junit" % "junit" % "4.12"
	val assertj = "org.assertj" % "assertj-core" % "3.2.0"
	val scalaTest = "org.scalatest" %% "scalatest" % "2.2.6"
	val scalaParser = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
	val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.3.12"

	/**
	 * Projects
	 */
	val l = libraryDependencies
	val forTheImpatient = l ++= Seq(junit, assertj, scalaTest, scalaParser, akkaActor)
	val reactivePrograming = l ++= Seq(junit, assertj, scalaTest, akkaActor)
}