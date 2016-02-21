package be.arndep.scala.akka.util

import akka.actor.{Actor, ActorRef, Props}

/**
	* This actor can be used to check exchange message between parent and child actors
	*/
class FosterParent(child: Props, probe: ActorRef) extends Actor {
	val childRef = context.actorOf(child, "child")

	override def receive: Receive = {
		case msg if sender == context.parent =>
			probe forward msg
			childRef forward msg
		case msg =>
			probe forward msg
			context.parent forward msg
	}
}
