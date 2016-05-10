package training

import sbt.Keys._
import sbt._

object Dependencies {
	/**
		* versions
		*/
	object Version {
		val akka = "2.4.4"
	}

	object Akka {
		val actor = Akka("actor")
		val persistence = Akka("persistence")
		val cluster = Akka("cluster")
		val fsm = Akka("fsm")
		val testkit = Akka("testkit") % "test"

		def apply(artifact: String, version: String = Version.akka) = "com.typesafe.akka" %% s"akka-$artifact" % version
		def all = Seq(actor, persistence, cluster, fsm, testkit)
	}

	object Test {
		val junit = "junit" % "junit" % "4.12"
		val assertj = "org.assertj" % "assertj-core" % "3.4.1"
		val scalaTest = "org.scalatest" %% "scalatest" % "2.2.6"

		def all = Seq(junit, assertj, scalaTest)
	}

	object ScalaModule {
		val async = ScalaModule("async", "0.9.5")
		val parserCombinators = ScalaModule("parser-combinators", "1.0.4")

		def apply(artifact: String, version: String) = "org.scala-lang.modules" %% s"scala-$artifact" % version
	}

	/**
	 * Libraries
	 */
	val rxScala = "io.reactivex" %% "rxscala" % "0.26.1"
	val retrofit = "com.squareup.retrofit" % "retrofit" % "1.9.0"
	val asyncHttpClient = "com.ning" % "async-http-client" % "1.9.38"
	val jsoup = "org.jsoup" % "jsoup" % "1.9.1"

	/**
	 * Projects
	 */
	val l = libraryDependencies
	val forTheImpatient = l ++= Seq(ScalaModule.parserCombinators, Akka.actor) ++ Test.all
	val reactivePrograming = l ++= Seq(Akka.actor, rxScala, retrofit) ++ Test.all
	val akkaCourse = l ++= Seq(asyncHttpClient, jsoup) ++ Akka.all ++ Test.all
}