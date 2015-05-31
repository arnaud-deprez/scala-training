import sbt._

object Dependencies {
	/**
	 * Libraries
	 */
	val junit = "junit" % "junit" % "4.12"
	val assertj = "org.assertj" % "assertj-core" % "3.0.0"
	val scalaTest = "org.scalatest" % "scalatest_2.11" % "2.2.4"
	val scalaParser = "org.scala-lang.modules" % "scala-parser-combinators_2.11" % "1.0.4"
	val akkaActor = "com.typesafe.akka" % "akka-actor_2.11" % "2.3.11"

	/**
	 * Projects
	 */
	val forTheImpatientDependencies = Seq(junit, assertj, scalaTest, scalaParser, akkaActor)
}