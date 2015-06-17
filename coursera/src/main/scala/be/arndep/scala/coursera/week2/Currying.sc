//productRange with currying
def productRange(f: Int => Int)(a: Int, b: Int): Int =
	if (a > b) 1
	else f(a) * productRange(f)(a + 1, b)
productRange(x => x * x)(3, 4)

//factorial in term of product
def factorial(n: Int): Int = productRange(x => x)(1, n)
factorial(5)

//more general function for sum and product => mapReduce
def mapReduce(f: Int => Int, combine: (Int, Int) => Int, acc: Int)(a: Int, b: Int): Int =
	if (a > b) acc
	else combine(f(a), mapReduce(f, combine, acc)(a + 1, b))

def productRange2(f: Int => Int)(a: Int, b: Int): Int =
	mapReduce(f, (a, b) => a * b, 1)(a, b)
productRange2(x => x * x)(3, 4)

def factorial2(n: Int): Int = productRange2(x => x)(1, n)
factorial2(5)