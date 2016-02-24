package be.arndep.scala.akka.util

import akka.actor._
import akka.event.LoggingReceive

/**
	* This actor can be used to check exchange message between parent and child actors
	*/
class FosterParent(childProps: Props, probe: ActorRef) extends Actor
	with ActorLogging {
	val child = context.actorOf(childProps, "child")
	context.watch(child)

	override def receive: Receive = LoggingReceive {
		case Terminated(ref) if ref == child =>
			if (context.children.isEmpty)
				context stop self
		case msg if sender == context.parent =>
			log.debug("Forward to child {}", child)
			probe forward msg
			child forward msg
		case msg =>
			log.debug("Forward to parent {}", context.parent)
			probe forward msg
			context.parent forward msg
	}
}
