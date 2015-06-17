package week4

trait List[T] {
	def head: T
	def tail: List[T]
	def isEmpty: Boolean
}

class Cons[T](val head: T, val tail: List[T]) extends List[T] {
	override def isEmpty: Boolean = false
}

class Nil[T] extends List[T]{
	override def head: T = throw new NoSuchElementException("Nil.head")
	override def tail: List[T] = throw new NoSuchElementException("Nil.tail")
	override def isEmpty: Boolean = true
}

def nth[T](n: Int, xs: List[T]): T =
	if (xs.isEmpty) throw new IndexOutOfBoundsException
	else if (n == 0) xs.head
	else nth(n - 1, xs.tail)

val list = new Cons(1, new Cons(2, new Cons(3, new Nil)))
nth(2, list)
nth(4, list)
nth(-1, list)

object List {
	def apply[T](x1: T, x2: T): List[T] = new Cons(x1, new Cons(x2, new Nil))
	def apply[T](): List[T] = new Nil
}