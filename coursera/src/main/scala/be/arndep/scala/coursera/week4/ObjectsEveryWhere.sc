package week4

object True extends Boolean {
	override def ifThenElse(t: => Boolean, e: => Boolean): Boolean = t
	override def toString = "true"
}

object False extends Boolean {
	override def ifThenElse(t: => Boolean, e: => Boolean): Boolean = e
	override def toString = "false"
}

abstract class Boolean {
	//call-by-name
	def ifThenElse(t: => Boolean, e: => Boolean): Boolean

	def &&(x: => Boolean): Boolean = ifThenElse(x, False)
	def ||(x: => Boolean): Boolean = ifThenElse(True, x)
	def unary_! : Boolean = ifThenElse(False, True)

	def ==(x: => Boolean): Boolean = ifThenElse(x, x.unary_!)
	def !=(x: => Boolean): Boolean = ifThenElse(x.unary_!, x)

	def <(x: => Boolean): Boolean = ifThenElse(False, x)
}

object Booleans {
	val t = True
	val f = False
	t && f
	t || f
	!t

	t == t
	t != f
	f < t
}
