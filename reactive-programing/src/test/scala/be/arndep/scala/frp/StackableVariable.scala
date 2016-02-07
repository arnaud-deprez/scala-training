package be.arndep.scala.frp

/**
	* Created by arnaud.deprez on 20/01/16.
	*/
class StackableVariable[T](init: T) {
	private var values = List(init)

	def value = values.head

	def withValue[R](newValue: T)(op: => R): R = {
		values = newValue :: values
		try op finally values = values.tail
	}
}
