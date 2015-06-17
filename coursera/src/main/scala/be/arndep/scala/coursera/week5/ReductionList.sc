// Here we cannot use foldLeft because of type mismatch
def concat[T](xs: List[T], ys: List[T]): List[T] = (xs foldRight ys) (_ :: _)
concat(List(1, 2, 3), List(4, 5, 6))

def mapFun[T, U](xs: List[T], f: T => U): List[U] =
	(xs foldRight List[U]())((x, acc) => f(x) :: acc)
mapFun[Int, Int](List(1, 2, 3), x => x * x)

def lengthFun[T](xs: List[T]): Int =
	(xs foldRight 0)((x, acc) => acc + 1)
lengthFun(List(1, 2, 3))