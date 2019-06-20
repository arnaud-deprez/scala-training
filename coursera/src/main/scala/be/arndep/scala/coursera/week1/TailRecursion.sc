def factorial(n: BigInt): BigInt = {
	def loop(acc: BigInt, n: BigInt): BigInt = {
		if (n == 0) acc else loop(acc * n, n - 1)
	}
	loop(1, n)
}
factorial(4)