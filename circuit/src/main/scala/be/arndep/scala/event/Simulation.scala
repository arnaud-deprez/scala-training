package be.arndep.scala.event

/**
	* Created by arnaud.deprez on 16/01/16.
	*/
abstract class Simulation {
	//Type definitions
	type Action = () => Unit
	case class Event(time: Int, action: Action)
	private type Agenda = List[Event]
	private var agenda : Agenda = List()
	private var curtime = 0

	//private methods definition
	private def insert(ag: Agenda, item: Event): List[Event] = ag match {
		case first :: rest if first.time <= item.time => first :: insert(rest, item)
		case _ => item :: ag
	}
	private def loop(): Unit = agenda match {
		case first :: rest =>
			agenda = rest
			curtime = first.time
			first.action()
			loop()
		case Nil =>
	}

	//public methods definition
	def currentTime : Int = curtime
	def afterDelay(delay: Int)(block: => Unit): Unit = {
		val item = Event(currentTime + delay, () => block)
		agenda = insert(agenda, item)
	}
	def run(): Unit = {
		afterDelay(0) {
			println(s"*** Simulation started, time=$currentTime ***")
		}
		loop()
	}
}
