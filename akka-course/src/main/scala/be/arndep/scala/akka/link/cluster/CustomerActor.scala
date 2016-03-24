package be.arndep.scala.akka.link.cluster

import akka.actor._
import akka.remote.RemoteScope
import be.arndep.scala.akka.link.{ReceptionistActor, ControllerActor}

import scala.language.postfixOps
import scala.concurrent.duration._

/**
	* Created by arnaud.deprez on 1/03/16.
	*/
class CustomerActor(client: ActorRef, url: String, node: Address) extends Actor{
	//Change the meaning of who the sender is
	implicit val s = context.parent

	override val supervisorStrategy: SupervisorStrategy = SupervisorStrategy.stoppingStrategy
	val props = Props[ControllerActor].withDeploy(Deploy(scope = RemoteScope(node)))
	val controller = context.actorOf(props)
	context.watch(controller)

	context.setReceiveTimeout(5 seconds)
	controller ! ControllerActor.Check(url, 2)

	/**
		* Partial function.
		* Whatever the event happens (ReceiveTimeout, Terminated or Controller.Result), then we need to stop.
		* This stop is recursive as it will stop every child actors
		* @return
		*/
	override def receive = ({
		case ReceiveTimeout =>
			context.unwatch(controller)
			client ! ReceptionistActor.Failed(url, "controller timed out")
		case Terminated(_) =>
			client ! ReceptionistActor.Failed(url, "controller died")
		case ControllerActor.Result(links) =>
			context.unwatch(controller)
			client ! ReceptionistActor.Result(url, links)
	}: Receive) andThen(_ => context stop self)
}
