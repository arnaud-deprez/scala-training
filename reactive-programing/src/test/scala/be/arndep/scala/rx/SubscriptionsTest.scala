package be.arndep.scala.rx

import org.scalatest.FlatSpec
import rx.lang.scala.Subscription
import rx.lang.scala.subscriptions._

/**
	* Created by arnaud.deprez on 7/02/16.
	*/
class SubscriptionsTest extends FlatSpec {
	"A subscription" should "only be unsubscribed once" in {
		val subscription = Subscription {
			println("Bye, bye, I'm out fishing")
		}

		subscription.unsubscribe()
		println(subscription.isUnsubscribed)
		// bug!
		subscription.unsubscribe()
	}

	"CompositeSubscription" should "treat multiple subscription as one" in {
		val a = Subscription { println("Apple") }
		val b = Subscription { println("Banana") }
		val c = Subscription{ println ("Cranberry") }

		val composite = CompositeSubscription(a,b)

		println(s"composite.isUnsubscribed=${composite.isUnsubscribed}")

		composite.unsubscribe()

		println(s"a.isUnsubscribed=${a.isUnsubscribed}")
		println(s"b.isUnsubscribed=${b.isUnsubscribed}")
		println(s"composite.isUnsubscribed=${composite.isUnsubscribed}")

		println(s"c.isUnsubscribed=${c.isUnsubscribed}")
		composite += c
		println(s"c.isUnsubscribed=${c.isUnsubscribed}")

	}

	"MultipleAssignmentSubscription" should "allow us to assign multiple subscriptions" in {
		val a = Subscription { println("Apple") }
		val b = Subscription { println("Banana") }
		val c = Subscription{ println ("Cranberry") }

		val multiple = MultipleAssignmentSubscription()

		println(s"multiple.isUnsubscribed=${multiple.isUnsubscribed}")

		multiple.subscription = a
		multiple.subscription = b

		multiple.unsubscribe()

		println(s"a.isUnsubscribed=${a.isUnsubscribed}")
		println(s"b.isUnsubscribed=${b.isUnsubscribed}")
		println(s"multiple.isUnsubscribed=${multiple.isUnsubscribed}")

		println(s"c.isUnsubscribed=${c.isUnsubscribed}")
		multiple.subscription = c
		println(s"c.isUnsubscribed=${c.isUnsubscribed}")

	}
}
