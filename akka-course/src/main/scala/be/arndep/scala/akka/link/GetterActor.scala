package be.arndep.scala.akka.link

import akka.actor.{Actor, ActorLogging, Status}
import akka.pattern._
import org.jsoup.Jsoup

import scala.collection.JavaConversions._

/**
	* Created by arnaud.deprez on 18/02/16.
	*/
class GetterActor(url: String, depth: Int) extends Actor with ActorLogging {

	//The actors are run by dispatcher - potentially shared - and it can also be used to run Futures
	implicit val exec = context.dispatcher

	def client: WebClient = AsyncWebClient
	//Send the body if Success or the error wrapped in a status if Failure
	client get url pipeTo self

	def receive: Receive = {
		case body: String =>
			for (link <- findLinks(body))
				context.parent ! ControllerActor.Check(link, depth)
			context stop self
		case _: Status.Failure => context stop self
	}

	def findLinks(body: String): Iterator[String] = {
		val document = Jsoup.parse(body, url)
		val links = document.select("a[href]")
		for {
			link <- links.iterator()
		} yield link.absUrl("href")
	}
}

object GetterActor {
//	case class Done()
	case class Abort()
}
