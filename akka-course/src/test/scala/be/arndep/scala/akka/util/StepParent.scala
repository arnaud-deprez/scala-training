package be.arndep.scala.akka.util

import akka.actor._
import akka.event.LoggingReceive

/**
	* This actor is used to check message that are replied to parent
	*/
class StepParent(childProps: Props, probe: ActorRef) extends Actor with ActorLogging {

	override val supervisorStrategy: SupervisorStrategy = SupervisorStrategy.stoppingStrategy

	val child = context.actorOf(childProps, "child")
	context.watch(child)

	override def receive: Receive = LoggingReceive {
		case Terminated(ref) if ref == child =>
			if (context.children.isEmpty)
				context stop self
		case msg if sender == child => probe forward msg
		case msg => child forward msg
	}
}
