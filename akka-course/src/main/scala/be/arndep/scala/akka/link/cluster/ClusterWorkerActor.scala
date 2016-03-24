package be.arndep.scala.akka.link.cluster

import akka.actor.Actor
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
class ClusterWorkerActor extends Actor{
	val cluster = Cluster(context.system)
	cluster.subscribe(self, classOf[ClusterEvent.MemberRemoved])
	val main = cluster.selfAddress.copy(port = Some(2552))
	cluster.join(main)

	def receive = {
		case ClusterEvent.MemberRemoved(m, _) =>
			if (m.address == main) context stop self
	}

	@scala.throws[Exception](classOf[Exception])
	override def postStop(): Unit = AsyncWebClient.shutdown()
}
