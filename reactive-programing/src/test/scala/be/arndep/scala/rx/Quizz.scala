package be.arndep.scala.rx

import org.junit.Test
import org.scalatest.junit.JUnitSuite
import rx.lang.scala.Observable
import org.junit.Assert._

/**
	* Created by arnaud.deprez on 7/02/16.
	*/
class Quizz extends JUnitSuite{
	@Test def quizI(): Unit = {

		val xs = Observable.from(1 to 10)
		val ys = xs.map(x => x+1)

		assertEquals(List(2,3,4,5,6,7,8,9,10,11), ys.toBlocking.toList)
	}
}
