package be.arndep.scala.rx

import org.junit.Test
import org.scalatest.junit.JUnitSuite
import rx.lang.scala.{Observable, Subscription}

//import for scala duration
import scala.concurrent.duration._
import scala.language.postfixOps

/**
	* Created by arnaud.deprez on 7/02/16.
	*/
class HelloObservablesTest extends JUnitSuite {
	@Test def ticks(): Unit = {

		val ticks: Observable[Long]        = Observable.interval(1 second)
		val evens: Observable[Long]        = ticks.filter(s => s%2 == 0)
		val buffers: Observable[Seq[Long]] = evens.slidingBuffer(2,1)

		// run the program for a while
		val subscription: Subscription     = buffers.subscribe(println(_))

		Thread sleep((10 second).toMillis)

		// stop the stream
		subscription.unsubscribe()
	}
}
