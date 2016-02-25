package be.arndep.scala.akka.link

import akka.actor._
import be.arndep.scala.akka.link.ControllerActor.{Check, Result}

import scala.concurrent.duration._
import scala.language.postfixOps

/**
	* Created by arnaud.deprez on 18/02/16.
	*/
class ControllerActor extends Actor with ActorLogging {
	//timer reset after each received message
	context.setReceiveTimeout(10 seconds)

	var cache = Set.empty[String]

	override val supervisorStrategy: SupervisorStrategy = OneForOneStrategy(maxNrOfRetries = 5) {
		case _: Exception => SupervisorStrategy.Restart
	}

	def getterProps(url: String, depth: Int): Props = Props(new GetterActor(url, depth - 1))

	def receive: Receive = {
		case Check(url, depth) =>
			log.debug("{} checking {}", depth, url)
			if (!cache(url) && depth > 0)
				context.watch(context.actorOf(getterProps(url, depth - 1)))
			cache += url
		case Terminated(ref) =>
			log.debug("Receive Terminated from {}", ref)
			if (context.children.isEmpty) {
				context.parent ! Result(cache)
			}
		case ReceiveTimeout => context.children foreach context.stop
	}
}

object ControllerActor {

	case class Check(url: String, depth: Int)

	case class Result(links: Set[String])

}
