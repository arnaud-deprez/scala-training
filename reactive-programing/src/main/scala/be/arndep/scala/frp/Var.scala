package be.arndep.scala.frp

/**
	* Created by arnaud.deprez on 21/01/16.
	*/
class Var[T](expr: => T) extends Signal[T](expr) {
	override def update(expr: => T): Unit = super.update(expr)
}

object Var {
	def apply[T](expr: => T) = new Var[T](expr)
}
