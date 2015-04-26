package be.arndep.scala.fortheimpatient.exercices

import be.arndep.scala.fortheimpatient.chapter11_operator._

/**
 * Created by Arnaud on 02-05-15.
 */
object Chapter11 extends App {
	// 1
	println(3 + 4 -> 5) // (7, 5)
	println(3 -> 4 * 5) // (3, 20)

	// 2
	println(BigInt(2) ^ BigInt(2)) // XOR operator
	// ** do not exist so I don't know why. Maybe to avoid confusion ?

	// 3
	class Fraction(n: Int, d: Int = 1) {
		val num = if (d == 0) 1 else n * sign(d) / gcd(n, d)
		val den = if (d == 0) throw new IllegalArgumentException("den = 0!") else d * sign(d) / gcd(n, d)

		def unary_- = new Fraction(-this.num, this.den)
		def unary_~ = new Fraction(this.den, this.num)

		def +(that: Fraction): Fraction = {
			val m = lcm(this.den, that.den)
			new Fraction(m / this.den * this.num + m / that.den * that.num, m)
		}
		def -(that: Fraction): Fraction = this + (-that)
		def *(that: Fraction): Fraction = new Fraction(this.num * that.num, this.den * that.den)
		def /(that: Fraction): Fraction = this * (~that)
		override def toString: String = num + "/" + den
	}
	object Fraction {
		def apply(n: Int, d: Int = 1): Fraction = new Fraction(n, d)
		def unapply(in: Fraction) = if (in.den == 0) None else Some((in.num, in.den))
	}
	{
		println(Fraction(2))
		println(Fraction(15, 10))
		println(Fraction(-15, -10))
		println(Fraction(2, 3) + Fraction(1, 4))
		println(Fraction(2, 3) - Fraction(1, 4))
	}

	// 4
	object Money {
		def apply(d: Int, c: Int = 0) = new Money(d, c)
		def unapply(in: Money) = Some(in.dollars, in.cents)
	}

	class Money(val d: Int, val c: Int = 0) {
		val total: Int = d * 100 + c
		val dollars: Int = total / 100
		val cents: Int = total % 100

		override val toString = dollars + "$ " + cents + "¢"

		def +(that: Money) = new Money(this.dollars + that.dollars, this.cents + that.cents)
		def -(that: Money) = new Money(this.dollars - that.dollars, this.cents - that.cents)

		def <(that: Money) = this.total < that.total
		def <=(that: Money) = this.total <= that.total
		def >(that: Money) = this.total > that.total
		def >=(that: Money) = this.total >= that.total
		override def equals(that: Any): Boolean = {
			if (that.isInstanceOf[Money])
				this.total == that.asInstanceOf[Money].total
			else
				false
		}

		override def hashCode(): Int = this.total.hashCode
	}

	{
		println(Money(-1, 270))
		println(Money(-1, 270) + Money(2) - Money(0, 50))
		println(Money(-1, 270) == Money(-1, 270))
		println(Money(-1, 270) <= Money(-1, 271))
		println(Money(-1, 271) >= Money(-1, 271))
	}

	// 5
	object Table {
		def apply(html: String = "<table><tr>") = new Table(html)
	}

	class Table(html: String = "<table><tr>") {
		override val toString = html + "</tr></table>"

		def |(col: String) = new Table(html + "<td>" + col + "</td>")
		def ||(col: String) = new Table(html + "</tr><tr><td>" + col + "</td>")
	}

	{
		println(Table() | "Java" | "Scala" || "Gosling" | "Odersky" || "JVM" | "JVM, .NET")
	}

	// 6
	object ASCIIArt {
		def apply(name: String) = new ASCIIArt(io.Source.fromFile(name).mkString)
	}

	class ASCIIArt(fig: String) {
		override val toString = fig

		def *(that: ASCIIArt): ASCIIArt = {
			val linesA = this.toString.split("\n")
			val linesB = that.toString.split("\n")
			val linesCount = math.max(linesA.length, linesB.length)
			val maxLength = linesA.reduce((a, b) => if (a.length >= b.length) a else b) length
			var buf = new String

			for (i <- 0 until linesCount) {
				if (i < linesA.length)
					buf += linesA(i) + (" " * (maxLength - linesA(i).length + 1))
				if (i < linesB.length)
					buf += linesB(i)
				buf += "\n"
			}

			new ASCIIArt(buf)
		}

		def +(that: ASCIIArt): ASCIIArt = new ASCIIArt(this.toString + "\n\n" + that.toString)
	}

	{
		println(ASCIIArt("files/asciiart/cat.txt") + ASCIIArt("files/asciiart/scala.txt")) // vertical
		println(ASCIIArt("files/asciiart/cat.txt") * ASCIIArt("files/asciiart/scala.txt")) // horizontal
	}

	// 7
	class BitSequence {
		var b: Long = 0L

		override def toString = b.toBinaryString
		def apply(i: Int): Int = (((1L << i) & b) >> i).asInstanceOf[Int]
		def update(i: Int, value: Int) {
			if (value == 1)
				b |= (1L << i)
			else if (value == 0)
				b &= ~(1L << i)
		}
	}
	{
		val bits = new BitSequence
		bits(0) = 1
		bits(1) = 1
		bits(2) = 0
		bits(7) = 1
		println(bits)
		println(bits(7))
	}

	// 8
	object Matrix {
		def apply(m: Int, n: Int, f: (Int, Int) => Double) = new Matrix(m, n, f)
	}

	class Matrix(val m: Int, val n: Int, f: (Int, Int) => Double) {
		private val matrix = Array.tabulate[Double](m, n)(f)

		def apply(i: Int, j: Int): Double = this.matrix(i)(j)

		def +(that: Matrix): Matrix = {
			if (this.m != that.m || this.n != that.n)
				throw new IllegalArgumentException("Sizes of arrays don't match.")
			new Matrix(m, n, (i, j) => this(i, j) + that(i, j))
		}

		def *(that: Matrix): Matrix = {
			if (this.n != that.m)
				throw new IllegalArgumentException("Sizes of arrays don't match.")
				new Matrix(this.m, that.n, (i, j) => (for (k <- this.matrix(i).indices) yield this(i, k) * that(k, i)).sum)
		}

		def *(n: Double): Matrix = {
			this * new Matrix(1, 1, (i, j) => n)
		}

		override def toString: String = {
			var toString = "Matrix: [ "
			toString += matrix map(r => r.toList.toString) reduceLeft(_+ ", " + _)
			toString += " ]"
			toString
		}
	}
	{
		val m1 = Matrix(2, 2, (i, j) => (j + 1) + (i * 2))
		val m2 = Matrix(2, 2, (i, j) => (j + 1) + (i * 2) + 4)
		println(m1 + " + " + m2 + " = " + (m1 + m2))
		println(m1 + " * " + m2 + " = " + (m1 * m2))
	}

	// 9
	object RichFile {
		def apply(s: String) = new RichFile(s)
		def unapply(f: RichFile): Option[(String, String, String)] = {
			val fn = f.toString
			val slash = fn.lastIndexOf("/")
			val dot = fn.lastIndexOf(".")
			Some((fn.substring(0, slash+1), fn.substring(slash+1, dot), fn.substring(dot+1)))
		}
	}
	class RichFile(fileName: String) {
		override val toString = fileName
	}
	{
		val r = RichFile("/home/cay/readme.txt")
		val RichFile(path, file, ext) = r
		println("path: " + path + ", file: " + file + ", ext: " + ext)
	}

	// 10
	object RichFileSeq {
		def unapplySeq(f: RichFile): Option[Seq[String]] = Some(f.toString.split("/"))
	}
	{
		val r = RichFile("/home/cay/readme.txt")
		r match { case RichFileSeq(a, b, c, d) => println("match: " + a + b + c + d) }
		r match { case RichFileSeq(a @_*) => println("match: " + a) }
	}
}
