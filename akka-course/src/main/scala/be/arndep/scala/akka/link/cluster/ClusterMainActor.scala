package be.arndep.scala.akka.link.cluster

import akka.actor.{Actor, Props, ReceiveTimeout}
import akka.cluster.{Cluster, ClusterEvent}

import scala.concurrent.duration._
import scala.language.postfixOps

/**
	* @author arnaud.deprez
	* @since 23/03/16.
	*
	* Run configuration:
	* 	-Dakka.loglevel=INFO
	* 	-Dakka.actor.provider=akka.cluster.ClusterActorRefProvider
	* 	-Dakka.cluster.min-nr-of-members=2
	*/
class ClusterMainActor extends Actor {
	import be.arndep.scala.akka.link.ReceptionistActor._

	val cluster = Cluster(context.system)
	cluster.subscribe(self, classOf[ClusterEvent.MemberUp])
	cluster.subscribe(self, classOf[ClusterEvent.MemberRemoved])
	cluster.join(cluster.selfAddress)

	val receptionist = context.actorOf(Props[ClusterReceptionistActor], "receptionist")
	context watch receptionist

	def getLater(d: FiniteDuration, url: String): Unit = {
		import context.dispatcher
		context.system.scheduler.scheduleOnce(d, receptionist, Get(url))
	}

	getLater(Duration.Zero, "http://google.com")

	def receive = {
		case ClusterEvent.MemberUp(member) =>
			if (member.address != cluster.selfAddress) {
				getLater(1 seconds, "http://google.com")
				getLater(2 seconds, "http://google.com/0")
				getLater(2 seconds, "http://google.com/1")
				getLater(3 seconds, "http://google.com/2")
				getLater(4 seconds, "http://google.com/3")
				context.setReceiveTimeout(3 seconds)
			}
		case Result(url, set) =>
			println(set.toVector.sorted.mkString(s"Results for '$url':\n", "\n", "\n"))
		case Failed(url, reason) =>
			println(s"Failed to fetch '$url': $reason")
		case ReceiveTimeout =>
			cluster.leave(cluster.selfAddress)
		case ClusterEvent.MemberRemoved(m, _) =>
			context stop self
	}
}
