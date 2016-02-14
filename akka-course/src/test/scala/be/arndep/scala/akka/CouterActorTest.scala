package be.arndep.scala.akka

import akka.Main
import akka.actor.{Actor, Props}

/**
	* Created by arnaud.deprez on 14/02/16.
	*/
class MainActor extends Actor {
	val counter = context.actorOf(Props[CounterActor], "counter")

	counter ! "incr"
	counter ! "incr"
	counter ! "incr"
	counter ! "get"

	def receive = {
		case count: Int => println(s"count was $count")
			context.stop(self)
	}
}

/**
	* Created by arnaud.deprez on 14/02/16.
	*/
object CouterActorTest extends App {
	Main.main(Array("be.arndep.scala.akka.MainActor"))
}
