package be.arndep.scala.akka.link.cluster

import akka.actor.{Actor, ActorIdentity, ActorLogging, Identify, RootActorPath, Terminated}
import akka.cluster.{Cluster, ClusterEvent}
import be.arndep.scala.akka.link.AsyncWebClient

/**
	* @author arnaud.deprez
	* @since 1/03/16.
	*
	* Run configuration:
	* 	-Dakka.loglevel=INFO
	* 	-Dakka.actor.provider=akka.cluster.ClusterActorRefProvider
	* 	-Dakka.remote.netty.tcp.port=0
	* 	-Dakka.cluster.autodown=on
	*/
class ClusterWorkerActor extends Actor with ActorLogging {
	val cluster = Cluster(context.system)
	cluster.subscribe(self, classOf[ClusterEvent.MemberUp])
//	cluster.subscribe(self, classOf[ClusterEvent.MemberRemoved])
	val main = cluster.selfAddress.copy(port = Some(2552))
	cluster.join(main)

	def receive = {
		case ClusterEvent.MemberUp(member) =>
			if (member.address == main)
				context.actorSelection(RootActorPath(main) / "user" / "app" / "receptionist") ! Identify("42")
		case ActorIdentity("42", None) => context stop self
		case ActorIdentity("42", Some(ref)) =>
			log.info("receptionist is at {}", ref)
			context watch ref
		//The only message that is guaranteed in akka is Terminated!
		case Terminated(_) => context stop self
		/*case ClusterEvent.MemberRemoved(m, _) =>
			if (m.address == main) context stop self*/
	}

	@scala.throws[Exception](classOf[Exception])
	override def postStop(): Unit = AsyncWebClient.shutdown()
}
