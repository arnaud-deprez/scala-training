package be.arndep.scala.akka

import akka.actor.{ActorSystem, FSM, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

/**
	* @author arnaud.deprez 
	* @since 9/05/16
	*/
class FSMExampleActorSpec extends TestKit(ActorSystem("FSMExampleActorSpec"))
	with WordSpecLike with BeforeAndAfterAll with ImplicitSender {

	import FSMExampleActor._

	override protected def afterAll(): Unit = {
		println("in afterAll")
		system.terminate()
	}

	"simple finite state machine" must {

		"demonstrate NullFunction" in {
			class A extends FSM[Int, Null] {
				val SomeState = 0
				when(SomeState)(FSM.NullFunction)
			}
		}

		"batch correctly" in {
			val buncher = system.actorOf(Props(classOf[FSMExampleActor]))
			buncher ! SetTarget(testActor)
			buncher ! Queue(42)
			buncher ! Queue(43)
			expectMsg(Batch(Seq(42, 43)))
			buncher ! Queue(44)
			buncher ! Flush
			buncher ! Queue(45)
			expectMsg(Batch(Seq(44)))
			expectMsg(Batch(Seq(45)))
		}

		"not batch if uninitialized" in {
			val buncher = system.actorOf(Props(classOf[FSMExampleActor]))
			buncher ! Queue(42)
			expectNoMsg
		}
	}
}
