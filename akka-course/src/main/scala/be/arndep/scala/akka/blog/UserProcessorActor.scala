package be.arndep.scala.akka.blog

import akka.persistence.PersistentActor

/**
	* Created by arnaud.deprez on 23/02/16.
	*/
class UserProcessorSyncActor extends PersistentActor{
	import UserProcessorActor._

	var state = State(Vector.empty[String], false)

	override def receiveCommand: Receive = {
		case NewPost(text, id) =>
			if (state.disabled) sender ! BlogNotPosted(id, "quota reached")
			else {
				val created = PostCreated(text)
				//sync
				persist(created) { event =>
					updateState(event)
					sender ! BlogPosted(id)
				}
				persist(QuotaReached)(updateState)
			}
	}


	override def receiveRecover: Receive = { case e: Event => updateState(e) }

	def updateState(e: Event) { state = state.updated(e) }

	override def persistenceId: String = "userProcessorSync"
}

class UserProcessorASyncActor extends PersistentActor{
	import UserProcessorActor._

	var state = State(Vector.empty[String], false)

	override def receiveCommand: Receive = {
		case NewPost(text, id) =>
			if (state.disabled) sender ! BlogNotPosted(id, "quota reached")
			else {
				val created = PostCreated(text)
				updateState(created)
				updateState(QuotaReached)
				persistAsync(created)(_ => sender ! BlogPosted(id))
				persistAsync(QuotaReached)(_ => ())
			}
	}


	override def receiveRecover: Receive = { case e: Event => updateState(e) }

	def updateState(e: Event) { state = state.updated(e) }

	override def persistenceId: String = "userProcessorASync"
}

object UserProcessorActor {
	//Commands
	case class NewPost(text: String, id: Long)
	case class BlogPosted(id: Long)
	case class BlogNotPosted(id: Long, reason: String)

	//Events
	sealed trait Event
	case class PostCreated(text: String) extends Event
	object QuotaReached extends Event

	case class State(posts: Vector[String], disabled: Boolean) {
		def updated(e: Event): State = e match {
			case PostCreated(text) => copy(posts = posts :+ text)
			case QuotaReached => copy(disabled = true)
		}
	}
}
