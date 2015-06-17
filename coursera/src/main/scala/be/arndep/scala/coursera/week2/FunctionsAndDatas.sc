object Rationals {
	object Rational {
		def apply(x: Int, y: Int = 1) = new Rational(x, y)
	}
	class Rational(x: Int, y: Int) {
		require(y != 0, "denominator must be nonzero!")

		//other constructor
		def this(x: Int) = this(x, 1)

		//simplification
		private def gcd(a: Int, b: Int): Int = if (b == 0) math.abs(a) else gcd(b, a % b)
		private val g = gcd(x, y)

		val numer = x / g
		val denom = y / g

		def unary_- = new Rational(-numer, denom)

		def +(that: Rational) =
			new Rational(numer * that.denom + that.numer * denom,
				denom * that.denom)

		def -(that: Rational) = this + -that

		def <(that: Rational) = numer * that.denom < that.numer * denom
		def <=(that: Rational) = numer * that.denom <= that.numer * denom
		def >(that: Rational) = !(this < that)
		def >=(that: Rational) = !(this <= that)

		def max(that: Rational) = if (this < that) that else this

		override def toString = numer + "/" + denom
	}

	val x = Rational(1, 3)
	val y = Rational(5, 7)
	val z = Rational(3, 2)
	x - y - z
	y + y
	x < y
	x max y
//	val strange = Rational(1, 0)
}