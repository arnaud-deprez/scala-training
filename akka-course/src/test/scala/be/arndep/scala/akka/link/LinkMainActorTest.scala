package be.arndep.scala.akka.link

import akka.Main
import akka.actor.{ReceiveTimeout, Props, Actor}

import scala.concurrent.duration._
import scala.language.postfixOps

/**
	* Created by arnaud.deprez on 18/02/16.
	*/
class LinkMainActorTest extends Actor{
	import ReceptionistActor._

	val receptionist = context.actorOf(Props[ReceptionistActor], "receptionist")

	receptionist ! Get("http://google.be")

	context.setReceiveTimeout(10 seconds)

	def receive: Receive = {
		case Result(url, links) =>
			println(links.toVector.sorted.mkString(s"Results for $url:\n", "\n", "\n"))
		case Failed(url) =>
			println(s"Failed to fetch $url")
		case ReceiveTimeout =>
			context.stop(self)
	}

	@throws[Exception](classOf[Exception])
	override def postStop(): Unit = WebClient.shutdown()
}

object LinkMainActorTest extends App{
	Main.main(Array("be.arndep.scala.akka.link.LinkMainActorTest"))
}