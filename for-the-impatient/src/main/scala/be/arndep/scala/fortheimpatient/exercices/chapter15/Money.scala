package be.arndep.scala.fortheimpatient.exercices.chapter15

/**
 * Created by Arnaud on 04-05-15.
 */
class MoneyException extends Exception

case class Money (val amount: Double, val currency: String) {
	@throws(clazz = classOf[MoneyException]) private def checkCurrency(that: Money) {
		if (currency != that.currency)
			throw new MoneyException()
	}

	def unary_- = this.copy(amount = -amount)

	@throws(clazz = classOf[MoneyException]) def +(that: Money): Money = {
		checkCurrency(that)
		this.copy(amount = amount + that.amount)
	}
	@throws(clazz = classOf[MoneyException]) def -(that: Money) = this + -that
}
