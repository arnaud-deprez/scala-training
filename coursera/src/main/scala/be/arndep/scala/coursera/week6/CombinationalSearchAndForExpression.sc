def scalarProduct1(xs: List[Double], ys: List[Double]): Double = {
	xs zip ys map(t => t._1 * t._2) sum
}

scalarProduct1(List(1, 2, 3, 4), List(5, 6, 7, 8))

def scalarProduct2(xs: List[Double], ys: List[Double]): Double = {
	(for ((x, y) <- xs zip ys) yield x * y).sum
}

scalarProduct2(List(1, 2, 3, 4), List(5, 6, 7, 8))