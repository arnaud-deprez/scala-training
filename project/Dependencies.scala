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
	val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.4.1"
	val rxScala = "io.reactivex" %% "rxscala" % "0.26.0"
	val retrofit = "com.squareup.retrofit" % "retrofit" % "1.9.0"
	val scalaAsync = "org.scala-lang.modules" %% "scala-async" % "0.9.5"
	val asyncHttpClient = "com.ning" % "async-http-client" % "1.9.33"
	val jsoup = "org.jsoup" % "jsoup" % "1.8.3"

	/**
	 * Projects
	 */
	val l = libraryDependencies
	val forTheImpatient = l ++= Seq(junit, assertj, scalaTest, scalaParser, akkaActor)
	val reactivePrograming = l ++= Seq(junit, assertj, scalaTest, akkaActor, rxScala, retrofit)
	val akkaCourse = l ++= Seq(akkaActor, asyncHttpClient, jsoup, junit, assertj, scalaTest)
}