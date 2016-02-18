package be.arndep.scala.akka.link

import akka.actor._
import be.arndep.scala.akka.link.ControllerActor.{Result, Check}

import scala.concurrent.duration._
import scala.language.postfixOps

/**
	* Created by arnaud.deprez on 18/02/16.
	*/
class ControllerActor extends Actor with ActorLogging {
	//timer reset after each received message
	context.setReceiveTimeout(10 seconds)

	var cache = Set.empty[String]
	var children = Set.empty[ActorRef]

	def receive: Receive = {
		case Check(url, depth) =>
			log.debug("{} checking {}", depth, url)
			if (!cache(url) && depth > 0)
				children += context.actorOf(Props(new GetterActor(url, depth - 1)))
			cache += url
		case GetterActor.Done =>
			children -= sender
			if (children.isEmpty)
				context.parent ! Result(cache)
			case ReceiveTimeout => children foreach(_ ! GetterActor.Abort)
	}

}

object ControllerActor {
	case class Check(url: String, depth: Int)
	case class Result(links: Set[String])
}
