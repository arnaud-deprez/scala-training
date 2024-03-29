package be.arndep.scala.akka.link

import akka.actor.{Actor, ActorSystem, Props}
import akka.event.LoggingReceive
import akka.testkit.{ImplicitSender, TestKit}
import be.arndep.scala.akka.link.ReceptionistActor.Job
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

import scala.language.postfixOps
import scala.concurrent.duration._

/**
	* Created by arnaud.deprez on 21/02/16.
	*/
class ReceptionistActorSpec extends TestKit(ActorSystem("ReceptionistActorSpec"))
	with WordSpecLike with BeforeAndAfterAll with ImplicitSender {

	import ReceptionistActor._
	import ReceptionistActorSpec._

	override protected def afterAll(): Unit = system.terminate()

	"A Receptionist" must {
		"reply with a result" in {
			val receptionist = system.actorOf(fakeReceptionist, "sendResult")
			receptionist ! Get("myUrl")
			expectMsg(Result("myUrl", Set("myUrl")))
		}

		"reject request flood" in {
			val receptionist = system.actorOf(fakeReceptionist, "rejectFlood")
			for (i <- 1 to 5) receptionist ! Get(s"myUrl$i")
			expectMsg(Failed("myUrl5", "Reach maximum job size"))
			for (i <- 1 to 4) expectMsg(Result(s"myUrl$i", Set(s"myUrl$i")))
		}
	}
}

object ReceptionistActorSpec {
	class FakeController extends Actor {
		import context.dispatcher
		override def receive: Receive = {
			case ControllerActor.Check(url, depth) =>
				context.system.scheduler.scheduleOnce(1 seconds, sender, ControllerActor.Result(Set(url)))
		}
	}

	def fakeReceptionist: Props =
		Props(new ReceptionistActor {
			override def controllerProps: Props = Props[FakeController]

			override def runNext(queue: Vector[Job]): Receive = LoggingReceive { super.runNext(queue) }

			override def enqueueJob(queue: Vector[Job], job: Job): Receive = LoggingReceive { super.enqueueJob(queue, job) }

			override def waiting: Receive = LoggingReceive { super.waiting }

			override def running(queue: Vector[Job]): Receive = LoggingReceive { super.running(queue) }
		})
}
