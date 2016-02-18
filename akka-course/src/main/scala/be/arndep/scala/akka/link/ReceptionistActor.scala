package be.arndep.scala.akka.link

import akka.actor.{Props, ActorRef, Actor}
import be.arndep.scala.akka.link.ReceptionistActor.{Failed, Result, Get, Job}

/**
	* Created by arnaud.deprez on 18/02/16.
	*/
class ReceptionistActor extends Actor {
	var reqNo = 0

	def runNext(queue: Vector[Job]): Receive = {
		reqNo += 1
		if (queue.isEmpty) waiting
		else {
			val controller = context.actorOf(Props[ControllerActor], s"c$reqNo")
			controller ! ControllerActor.Check(queue.head.url, 2)
			running(queue)
		}
	}

	def enqueueJob(queue: Vector[Job], job: Job): Receive = {
		if (queue.size > 3) {
			sender ! Failed(job.url)
			running(queue)
		}
		else running(queue :+ job)
	}

	def waiting: Receive = {
		case Get(url) => context.become(runNext(Vector(Job(sender, url))))
	}

	def running(queue: Vector[Job]): Receive = {
		case ControllerActor.Result(links) =>
			val job = queue.head
			job.client ! Result(job.url, links)
			context stop sender
			context become(runNext(queue.tail))
		case Get(url) =>
			context.become(enqueueJob(queue, Job(sender, url)))
	}

	def receive: Receive = waiting
}

object ReceptionistActor {
	case class Get(url: String)
	case class Job(client: ActorRef, url: String)
	case class Result(url: String, links: Set[String])
	case class Failed(url: String)
}
