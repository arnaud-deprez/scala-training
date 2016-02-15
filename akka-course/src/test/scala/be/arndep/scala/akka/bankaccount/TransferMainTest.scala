package be.arndep.scala.akka.bankaccount

import akka.Main
import akka.actor.{Props, Actor}
import akka.event.LoggingReceive

/**
	* Created by arnaud.deprez on 14/02/16.
	*/
class TransferMainTest extends Actor {
	val accountA = context.actorOf(Props[BankAccount], "accountA")
	val accountB = context.actorOf(Props[BankAccount], "accountB")

	accountA ! BankAccount.Deposit(100)

	def receive = LoggingReceive {
		case BankAccount.Done => transfer(50)
	}

	def transfer(amount: BigInt): Unit = {
		val transaction = context.actorOf(Props[WireTransfer], "transfer")
		transaction ! WireTransfer.Transfer(accountA, accountB, amount)
		context.become(LoggingReceive {
			case WireTransfer.Done =>
				println("success")
				context.stop(self)
			case WireTransfer.Failed =>
				println("failed")
				context.stop(self)
		})
	}
}

object TransferMainTest extends App{
	Main.main(Array("be.arndep.scala.akka.bankaccount.TransferMainTest"))
}
