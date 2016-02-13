package be.arndep.scala.rx

import be.arndep.scala.rx.Utils._
import org.scalatest.{FlatSpec, Matchers}
import rx.lang.scala.Observable

//import for scala duration
import scala.concurrent.duration._
import scala.language.postfixOps

/**
	* Created by arnaud.deprez on 7/02/16.
	*/
class NestedObservablesTest extends FlatSpec with Matchers {

	"The flatten operation on Observable" should "flatten the nested stream into one stream (non blocking)" in {
		val xs: Observable[Int]              = Observable.from(List(3, 2, 1))
		val yss: Observable[Observable[Int]] = xs.map(x => Observable.interval(x seconds).map(_ => x).take(2))
		val zs = yss.flatten

		val list = zs.toBlocking.toList
		val isFirst = list == List(1, 1, 2, 3, 2, 3)
		val isSecond = list == List(1, 2, 1, 3, 2, 3)
		// behavior of flatten is non-deterministic
		true should be (isFirst || isSecond)
		if (isFirst) println("first option")
		if (isSecond) println("second option")
		displayObservable(zs)
	}

	"The concat operation on Observable" should "concat the nested stream into one stream (blocking)" in {
		val xs: Observable[Int]              = Observable.from(List(3, 2, 1))
		val yss: Observable[Observable[Int]] = xs.map(x => Observable.interval(x seconds).map(_ => x).take(2))
		val zs = yss.concat

		zs.toBlocking.toList should be (List(3, 3, 2, 2, 1, 1))
		displayObservable(zs)
	}

	/**
		* The marble diagram on the slides is not entirely correct, but the final Observable zs is correct.
		* Interested students can investigate what happens in between using this test.
		* The difference is that Observable.interval produces a cold Observable, which only starts emitting
		* values once concat subscribes to it. So there is no buffering here, and concat is only subscribed
		* to one inner observable at the same time.
		*/
	"The concat operation on Observable explained" should "print details" in {
		val t0 = System.currentTimeMillis

		val xs: Observable[Int] = Observable.from(List(3, 2, 1))
		val yss: Observable[Observable[Int]] =
			xs map { x => Observable.interval(x seconds).doOnEach(
				n => println(f"${(System.currentTimeMillis-t0)/1000.0}%.3f:Observable.interval($x seconds) emits $n")
			).map(_ => x).take(2) }
		val zs: Observable[Int] = yss.concat

		displayObservable(zs)
	}
}
