package be.arndep.scala.akka.link.cluster

import akka.actor._
import akka.cluster.Cluster
import akka.cluster.ClusterEvent.{CurrentClusterState, MemberRemoved, MemberUp}
import be.arndep.scala.akka.link.{ControllerActor, ReceptionistActor}

import scala.util.Random

/**
	* Created by arnaud.deprez on 18/02/16.
	*/
class ClusterReceptionistActor extends Actor {
	import ClusterReceptionistActor._
	import ReceptionistActor._

	val cluster = Cluster(context.system)
	cluster.subscribe(self, classOf[MemberUp])
	cluster.subscribe(self, classOf[MemberRemoved])
	var reqNo = 0

	override val supervisorStrategy: SupervisorStrategy = SupervisorStrategy.stoppingStrategy

	@throws[Exception](classOf[Exception])
	override def postStop(): Unit = cluster.unsubscribe(self)

	val awaitingMember: Receive = {
		//Response to subscribe
		case current: CurrentClusterState =>
			val addresses = current.members.toVector map (_.address)
			val notMe = addresses filter (_ != cluster.selfAddress)
			if (notMe.nonEmpty) context.become(active(notMe))
		case MemberUp(member) if member.address != cluster.selfAddress =>
			context.become(active(Vector(member.address)))
		case Get(url) => sender ! Failed(url, "No nodes availables")
	}

	def active(addresses: Vector[Address]): Receive = {
		case MemberUp(member) if member.address != cluster.selfAddress =>
			context.become(active(addresses :+ member.address))
		case MemberRemoved(member, _) =>
			val next = addresses filterNot(_ == member.address)
			if (next.isEmpty) context.become(awaitingMember)
		else context.become(active(next))
		case Get(url) if context.children.size < addresses.size =>
			//We have to copy values because the actor creation is performed asynchronously!
			val client = sender
			val address = pick(addresses)
			context.actorOf(Props(new CustomerActor(client, url, address)))
		case Get(url) =>
			sender ! Failed(url, "too many parallel queries")
	}


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

	def receive: Receive = awaitingMember
}

object ClusterReceptionistActor {
	def pick(addresses: Vector[Address]): Address = Random.shuffle(addresses).head
}
