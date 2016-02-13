package be.arndep.scala.rx

import org.scalatest.FlatSpec
import rx.lang.scala.Observable

//import for scala duration
import scala.concurrent.duration._
import scala.language.postfixOps

/**
	* Created by arnaud.deprez on 7/02/16.
	*/
class BlockingTest extends FlatSpec {
	// List(0, 1, 2, 3, 4)
	// 10
	//Don'tt Do This At Home Kids
	"The toBlocking operation on Observable" should "block until completion" in {
		val xs: Observable[Long] = Observable.interval(1 second).take(5)

		val ys: List[Long] = xs.toBlocking.toList
		println(ys)

		val zs: Observable[Long] = xs.sum

		val sum: Long = zs.toBlocking.single
		println(sum)
	}

}
