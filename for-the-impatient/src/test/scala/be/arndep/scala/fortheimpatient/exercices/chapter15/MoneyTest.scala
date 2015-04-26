package be.arndep.scala.fortheimpatient.exercices.chapter15

import org.assertj.core.api.Assertions
import org.junit.{Before, Test}
import org.scalatest.junit.AssertionsForJUnit

/**
 * Created by Arnaud on 04-05-15.
 */
class MoneyTest extends AssertionsForJUnit {
	private var chf1: Money = _
	private var chf2: Money = _
	private var usd1: Money = _

	@Before
	def setUp() {
		chf1 = Money(12, "CHF")
		chf2 = Money(14, "CHF")
		usd1 = Money(28, "USD")
	}

	@Test
	def testUnary_-() {
		println("in testUnary_-")
		val chf1b = -chf1;
		Assertions.assertThat(chf1b.currency)
			.isEqualTo(chf1.currency)
		Assertions.assertThat(chf1b.amount)
			.isEqualTo(-chf1.amount)
	}

	@Test
	def testAddition() {
		println("in testAddition")
		val sum = chf1 + chf2
		Assertions.assertThat(sum.currency)
			.isEqualTo(chf1.currency)
		Assertions.assertThat(sum.amount)
			.isEqualTo(chf1.amount + chf2.amount)
	}

	@Test
	def testSubstraction() {
		println("in testSubstraction")
		val sum = chf1 - chf2
		Assertions.assertThat(sum.currency)
			.isEqualTo(chf1.currency)
		Assertions.assertThat(sum.amount)
			.isEqualTo(chf1.amount - chf2.amount)
	}

	@Test(expected = classOf[MoneyException])
	def testError() {
		println("in testError")
		val sum = chf1 - usd1
	}
}
