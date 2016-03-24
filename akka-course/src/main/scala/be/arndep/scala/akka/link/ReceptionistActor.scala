package be.arndep.scala.akka.link

import akka.actor._
import be.arndep.scala.akka.link.ReceptionistActor.{Failed, Get, Job, Result}

/**
	* Created by arnaud.deprez on 18/02/16.
	*/
class ReceptionistActor extends Actor {
	var reqNo = 0

	override val supervisorStrategy: SupervisorStrategy = SupervisorStrategy.stoppingStrategy

	def controllerProps: Props = Props[ControllerActor]

	def runNext(queue: Vector[Job]): Receive = {
		reqNo += 1
		if (queue.isEmpty) waiting
		else {
			val controller = context.actorOf(controllerProps, s"c$reqNo")
			context.watch(controller)
			controller ! ControllerActor.Check(queue.head.url, 2)
			running(queue)
		}
	}

	def enqueueJob(queue: Vector[Job], job: Job): Receive = {
		if (queue.size > 3) {
			sender ! Failed(job.url, "Reach maximum job size")
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
			context stop (context.unwatch(sender))
			context become (runNext(queue.tail))
		case Terminated(_) =>
			val job = queue.head
			job.client ! Failed(job.url, "Error")
			context become (runNext(queue.tail))
		case Get(url) =>
			context.become(enqueueJob(queue, Job(sender, url)))
	}

	def receive: Receive = waiting
}

object ReceptionistActor {

	case class Get(url: String)

	case class Job(client: ActorRef, url: String)

	case class Result(url: String, links: Set[String])

	case class Failed(url: String, message: String)

}
