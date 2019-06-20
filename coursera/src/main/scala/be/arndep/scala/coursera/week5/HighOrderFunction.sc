package week5

// 1
object SquareList {
	def squareList1(xs: List[Int]): List[Int] = xs match {
		case Nil => xs
		case y :: ys => y * y :: squareList1(ys)
	}

	def squareList2(xs: List[Int]): List[Int] = xs.map(i => i * i)

	val list = List(1, 2, 3, 4)
	squareList1(list)
	squareList2(list)
}

// 2
def pack[T](xs: List[T]): List[List[T]] = xs match {
	case Nil      => Nil
	case x :: xs1 =>
		val (firsts, res) = xs span(_ == x)
		firsts :: pack(res)
}
//List(List("a", "a", "a"), List("b"), List("c", "c"), List("a"))
val data = List("a", "a", "a", "b", "c", "c", "a")
pack(data)

def encode[T](xs: List[T]): List[(T, Int)] = {
	pack(xs) map(ys => (ys.head, ys.length))
}
encode(data)