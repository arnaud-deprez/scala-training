package be.arndep.scala.rx

import org.junit.Test
import org.scalatest.junit.JUnitSuite
import rx.lang.scala.Observable

//import for scala duration
import scala.concurrent.duration._
import scala.language.postfixOps

/**
	* Created by arnaud.deprez on 7/02/16.
	*/
class BlockingTest extends JUnitSuite {
	// List(0, 1, 2, 3, 4)
	// 10
	@Test def dontDoThisAtHomeKids(): Unit = {

		val xs: Observable[Long] = Observable.interval(1 second).take(5)

		val ys: List[Long] = xs.toBlocking.toList
		println(ys)

		val zs: Observable[Long] = xs.sum

		val sum: Long = zs.toBlocking.single
		println(sum)

	}

}
