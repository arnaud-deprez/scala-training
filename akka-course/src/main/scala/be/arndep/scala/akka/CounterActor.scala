package be.arndep.scala.akka

import akka.actor.Actor

/**
	* State change explicitly (by method become)
	* State is scoped to current behavior
	*
	* Asynchronous tail recursion
	*/
class CounterActor extends Actor {
	def counter(n: Int): Receive = {
		//update state
		case "incr" => context.become(counter(n + 1))
		case "get" => sender ! n
	}

	/**
		* Define the initial state
		*
		* @return
		*/
	def receive = counter(0)
}
