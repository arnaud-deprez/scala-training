package be.arndep.scala.rx

import org.scalatest.{FlatSpec, Matchers}
import rx.lang.scala.Observable

/**
	* Created by arnaud.deprez on 7/02/16.
	*/
class QuizzTest extends FlatSpec with Matchers{
	it should "add 1 to the generated number" in {
		val xs = Observable.from(1 to 10)
		val ys = xs.map(x => x+1)

		ys.toBlocking.toList should be (List(2,3,4,5,6,7,8,9,10,11))
	}
}
