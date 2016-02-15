package be.arndep.scala.akka.bankaccount

import akka.actor.{Actor, ActorRef}
import akka.event.LoggingReceive

/**
	* Created by arnaud.deprez on 14/02/16.
	*/
object WireTransfer {
	case class Transfer(from: ActorRef, to: ActorRef, amount: BigInt)
	case object Done
	case object Failed
}

class WireTransfer extends Actor {
	import WireTransfer._

	def receive = LoggingReceive {
		case Transfer(from, to, amount) =>
			from ! BankAccount.Withdraw(amount)
			context.become(awaitFrom(to, amount, sender))
	}

	def awaitFrom(to: ActorRef, amount: BigInt, customer: ActorRef): Receive = LoggingReceive {
		case BankAccount.Done =>
			to ! BankAccount.Deposit(amount)
			context.become(awaitTo(customer))
		case BankAccount.Failed =>
			customer ! Failed
			context.stop(self)
	}

	def awaitTo(customer: ActorRef): Receive = LoggingReceive {
		case BankAccount.Done =>
			customer ! Done
			context.stop(self)
	}
}
