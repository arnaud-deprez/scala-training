def streamRange(lo: Int, hi: Int): Stream[Int] = {
	print(lo+" ")
	if (lo >= hi) Stream.empty
	else Stream.cons(lo, streamRange(lo + 1, hi))
}
streamRange(1, 10).take(3).toList

//Lazy evaluation
def isPrime(n: Int): Boolean = (2 until n) forall(x => n%x != 0)
(streamRange(1000, 10000) filter isPrime) apply 1