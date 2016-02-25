package be.arndep.scala.akka.link

import akka.actor.{Status, Actor, Props, ActorSystem}
import akka.event.LoggingReceive
import akka.testkit.{ImplicitSender, TestKit}
import be.arndep.scala.akka.link.ControllerActor.{Result, Check}
import be.arndep.scala.akka.util.{FosterParent, StepParent}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

/**
	* Created by arnaud.deprez on 24/02/16.
	*/
class ControllerActorSpec extends TestKit(ActorSystem("ControllerActorSpec"))
	with WordSpecLike with BeforeAndAfterAll with ImplicitSender {

	import ControllerActorSpec._

	override protected def afterAll(): Unit = {
		println("in afterAll")
		system.terminate()
	}

	"A ControllerActor" must {

		"return a result with the list of links" in {
			val controller = system.actorOf(Props(new StepParent(fakeController, testActor)), "controllerResult")
			watch(controller)
			controller ! Check("myUrl", 2)
			expectMsg(Result(Set("myUrl", "myUrl/1", "myUrl/1/0")))
			expectTerminated(controller)
		}
	}
}

object ControllerActorSpec {
	class FakeGetter(url: String, depth: Int) extends Actor {

		self ! s"$url/$depth"

		override def receive: Receive = LoggingReceive {
			case body: String =>
				context.parent ! ControllerActor.Check(s"$url/$depth", depth)
				context stop self
			case _: Status.Failure => context stop self
		}
	}

	def fakeController: Props =
		Props(new ControllerActor() {
			override def getterProps(url: String, depth: Int): Props = Props(new FakeGetter(url, depth))

			override def receive: Receive = LoggingReceive { super.receive }
		})
}
