def isPrime(n: Int): Boolean = (2 until n) forall(x => n%x != 0)
isPrime(1)
isPrime(3)
isPrime(4)