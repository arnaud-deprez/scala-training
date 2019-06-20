val x = 0
def f(y: Int) = y +1
//result = 16
val result = {
	val x = f(3); x * x
} + x
