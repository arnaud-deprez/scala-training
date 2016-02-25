package be.arndep.scala.akka.link

import akka.Main
import akka.actor.{ReceiveTimeout, Props, Actor}
import akka.event.LoggingReceive

import scala.concurrent.duration._
import scala.language.postfixOps

/**
	* Created by arnaud.deprez on 18/02/16.
	*/
class LinkMainActorTest extends Actor{
	import ReceptionistActor._

	val receptionist = context.actorOf(Props[ReceptionistActor], "receptionist")
	context.watch(receptionist)

	receptionist ! Get("http://google.be")

	context.setReceiveTimeout(10 seconds)

	def receive: Receive = LoggingReceive {
		case Result(url, links) =>
			println(links.toVector.sorted.mkString(s"Results for $url:\n", "\n", "\n"))
		case Failed(url) =>
			println(s"Failed to fetch $url")
		case ReceiveTimeout =>
			context.stop(self)
	}
}

object LinkMainActorTest extends App{
	Main.main(Array("be.arndep.scala.akka.link.LinkMainActorTest"))
}
