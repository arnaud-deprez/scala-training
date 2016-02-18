package be.arndep.scala.akka.link

/**
	* Created by arnaud.deprez on 18/02/16.
	*/
case class BadStatus(val message: String, val cause: Option[Throwable] =  None)
	extends Exception(message, cause.orNull)
