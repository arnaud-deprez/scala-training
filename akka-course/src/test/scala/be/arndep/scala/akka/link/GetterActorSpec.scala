package be.arndep.scala.akka.link

import java.util.concurrent.Executor

import akka.actor.{Props, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import be.arndep.scala.akka.util.StepParent
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}

import scala.concurrent.Future

/**
	* Created by arnaud.deprez on 21/02/16.
	*/
class GetterActorSpec extends TestKit(ActorSystem("GetterActorSpec"))
	with WordSpecLike with BeforeAndAfterAll with ImplicitSender {

	import GetterActorSpec._

	override protected def afterAll(): Unit = system.terminate()

	"A GetterActor" must {

		"return the right body" in {
			val getter = system.actorOf(Props(new StepParent(fakeGetterActor(firstLink, 2), testActor)), "rightBody")
			for (link <- links(firstLink))
				expectMsg(ControllerActor.Check(link, 2))
			expectMsg(GetterActor.Done)
		}

		"properly finish in case of error" in {
			val getter = system.actorOf(Props(new StepParent(fakeGetterActor("unknown", 2), testActor)), "wrongLink")
			expectMsg(GetterActor.Done)
		}
	}
}

object GetterActorSpec {
	val firstLink = "http://rkuhn.info/1"

	val bodies = Map(
		firstLink ->
			"""<html>
				|	<head><title>Page 1</title></head>
				| <body>
				| 	<h1>A link</h1>
				|   <a href="http://rkuhn.info/2">click here</a>
				| </body>
				|</html>""".stripMargin)

	val links = Map(firstLink -> Seq("http://rkuhn.info/2"))

	object FakeClient extends WebClient {
		override def get(url: String)(implicit exec: Executor): Future[String] =
			bodies get url match {
				case None => Future.failed(BadStatus(404.toString))
				case Some(body) => Future.successful(body)
			}
	}

	def fakeGetterActor(url: String, depth: Int): Props =
		Props(new GetterActor(url, depth) {
			override def client: WebClient = FakeClient
		})
}
