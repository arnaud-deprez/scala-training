package be.arndep.scala.akka

import akka.actor.{ActorRef, FSM}
import be.arndep.scala.akka.FSMExampleActor._

import scala.concurrent.duration._
import scala.language.postfixOps

/**
	* @author arnaud.deprez 
	* @since 9/05/16
	*/
class FSMExampleActor extends FSM[State, Data] {

	startWith(Idle, Uninitialized)

	when(Idle) {
		case Event(SetTarget(ref), Uninitialized) =>
			stay using Todo(ref, Vector.empty)
	}

	// transition elided ...
	onTransition {
		case Active -> Idle =>
			stateData match {
				case Todo(ref, queue) => ref ! Batch(queue)
				case _                => // nothing to do
			}
	}

	when(Active, stateTimeout = 1 second) {
		case Event(Flush | StateTimeout, t: Todo) =>
			goto(Idle) using t.copy(queue = Vector.empty)
	}

	// unhandled elided ...
	whenUnhandled {
		// common code for both states
		case Event(Queue(obj), t @ Todo(_, v)) =>
			goto(Active) using t.copy(queue = v :+ obj)

		case Event(e, s) =>
			log.warning("received unhandled request {} in state {}/{}", e, stateName, s)
			stay
	}

	initialize()
}

object FSMExampleActor {
	//Received events
	final case class SetTarget(ref: ActorRef)
	final case class Queue(obj: Any)
	case object Flush

	//Sent events
	final case class Batch(obj: Seq[Any])

	// states
	sealed trait State
	case object Idle extends State
	case object Active extends State

	sealed trait Data
	case object Uninitialized extends Data
	final case class Todo(target: ActorRef, queue: Seq[Any]) extends Data
}
