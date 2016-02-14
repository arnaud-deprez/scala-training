package be.arndep.scala.fortheimpatient.tests

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

/**
 * Created by Arnaud on 18-05-15.
 */
case class Greeting(who: String)

class GreetingActor extends Actor with ActorLogging {
	override def receive: Receive = {
		case Greeting(who) ⇒ log.info("Hello " + who)
		case _ => log.warning("Fuck you!")
	}
}

object GreetingActor extends App {
	val system = ActorSystem("MySystem")
	val hiActor = system.actorOf(Props[GreetingActor], name = "greeting")

	hiActor ! Greeting("Arnaud")
	hiActor ! "@#&!"

	system.terminate()
}
