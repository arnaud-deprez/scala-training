package be.arndep.scala.akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props, Terminated}
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike, Matchers}

class FaultHandlingDocSpec(_system: ActorSystem) extends TestKit(_system)
	with ImplicitSender with FlatSpecLike with Matchers with BeforeAndAfterAll {

	def this() = this(ActorSystem("FaultHandlingDocSpec",
		ConfigFactory.parseString(
			"""
      akka {
        loggers = ["akka.testkit.TestEventListener"]
        loglevel = "WARNING"
      }
			""")))

	override def afterAll {
		TestKit.shutdownActorSystem(system)
	}

	"A supervisor" must "apply the chosen strategy for its child" in {
		val supervisor = system.actorOf(Props[Supervisor], "supervisor")
		supervisor ! Props[Child]
		val child = expectMsgType[ActorRef] // retrieve answer from TestKit’s testActor

		child ! 42 // set state to 42
		child ! "get"
		expectMsg(42)

		child ! new ArithmeticException // crash it
		child ! "get"
		expectMsg(42)

		child ! new NullPointerException // crash it harder
		child ! "get"
		expectMsg(0)

		watch(child) // have testActor watch “child”
		child ! new IllegalArgumentException // break it
		expectMsgPF() { case Terminated(`child`) => () }
	}

	"In case of an unhandled error, a supervisor" should "not handle exception and escalate it" in {
		val supervisor = system.actorOf(Props[Supervisor], "supervisorNotHandled")
		supervisor ! Props[Child] // create new child
		val child = expectMsgType[ActorRef]

		watch(child)
		child ! "get" // verify it is alive
		expectMsg(0)

		child ! new Exception("CRASH") // escalate failure
		expectMsgPF() {
			case t @ Terminated(`child`) if t.existenceConfirmed => ()
		}
	}

	"A supervisor" should "not kill its children" in {
		val supervisor = system.actorOf(Props[Supervisor2], "supervisorNotKillChild")

		supervisor ! Props[Child]
		val child = expectMsgType[ActorRef]

		child ! 23
		child ! "get"
		expectMsg(23)

		child ! new Exception("CRASH")
		child ! "get"
		expectMsg(0)
	}
}

class Supervisor extends Actor {

	import akka.actor.OneForOneStrategy
	import akka.actor.SupervisorStrategy._

	import scala.concurrent.duration._

	override val supervisorStrategy =
		OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
			case _: ArithmeticException => Resume
			case _: NullPointerException => Restart
			case _: IllegalArgumentException => Stop
			case _: Exception => Escalate
		}

	def receive = {
		case p: Props => sender() ! context.actorOf(p)
	}
}

class Child extends Actor {
	var state = 0

	def receive = {
		case ex: Exception => throw ex
		case x: Int => state = x
		case "get" => sender() ! state
	}
}

class Supervisor2 extends Actor {
	import akka.actor.OneForOneStrategy
	import akka.actor.SupervisorStrategy._
	import scala.concurrent.duration._

	override val supervisorStrategy =
		OneForOneStrategy(maxNrOfRetries = 10, withinTimeRange = 1 minute) {
			case _: ArithmeticException      => Resume
			case _: NullPointerException     => Restart
			case _: IllegalArgumentException => Stop
			case _: Exception                => Escalate
		}

	def receive = {
		case p: Props => sender() ! context.actorOf(p)
	}
	// override default to not kill all children during restart
	override def preRestart(cause: Throwable, msg: Option[Any]) {}
}
