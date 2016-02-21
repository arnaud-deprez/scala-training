package be.arndep.scala.akka.util

import akka.actor.{Actor, ActorRef, Props}

/**
	* This actor is used to check message that are replied to parent
	*/
class StepParent(child: Props, probe: ActorRef) extends Actor {
	context.actorOf(child, "child")

	override def receive: Receive = {
		case msg => probe forward msg
	}
}
