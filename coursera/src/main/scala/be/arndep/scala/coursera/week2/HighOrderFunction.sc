def sumRange(f: Int => Int, a: Int, b: Int): Int = {
	def loop(a: Int, acc: Int): Int = {
		if (a > b) acc
		else loop(a + 1, f(a) + acc)
	}
	loop(a, 0)
}
sumRange(x => x * x, 3, 5) // 9 + 16 + 25 = 50