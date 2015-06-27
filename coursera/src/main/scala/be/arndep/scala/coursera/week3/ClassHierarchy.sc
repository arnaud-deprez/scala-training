abstract class IntSet{
	def contains(x: Int): Boolean
	def incl(x: Int): IntSet
	def union(that: IntSet): IntSet
}

//Singleton
object EmptyIntSet extends IntSet {
	override def contains(x: Int): Boolean = false
	override def incl(x: Int): IntSet = new NonEmptyIntSet(x, EmptyIntSet, EmptyIntSet)
	override def union(that: IntSet): IntSet = that
	override def toString = "."
}

class NonEmptyIntSet(elem: Int, left: IntSet, right: IntSet) extends IntSet {
	override def contains(x: Int): Boolean =
		if (x < elem) left contains x
		else if (x > elem) right contains x
		else true

	override def incl(x: Int): IntSet =
		if (x < elem) new NonEmptyIntSet(elem, left incl x, right)
		else if (x > elem) new NonEmptyIntSet(elem, left, right incl x)
		else this

	override def union(that: IntSet): IntSet =
		((left union right) union that) incl elem

	override def toString = "{" + left + elem + right + "}"
}

val t1 = new NonEmptyIntSet(3, EmptyIntSet, EmptyIntSet)
val t2 = t1 contains 4
val t3 = t1 incl 2 incl 10
t3 union new NonEmptyIntSet(5, EmptyIntSet, EmptyIntSet)