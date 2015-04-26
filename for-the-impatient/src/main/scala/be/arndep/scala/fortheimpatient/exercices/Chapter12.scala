package be.arndep.scala.fortheimpatient.exercices

/**
 * Created by Arnaud on 02-05-15.
 */
object Chapter12 extends App{
	// 1
	{
		def values(fun: (Int) => Int, low: Int, high: Int): Seq[(Int, Int)] = {
			for (i <- low to high) yield (i, fun(i))
		}
		println(values(x => x*x, -5, 5))
	}

	// 2
	{
		println(1 to 10 reduceLeft((a, b) => if (a >= b) a else b))
	}

	// 3
	{
		def fact(n: BigInt): BigInt = if (n < 1) 1 else BigInt(2) to n reduceLeft(_ * _)
		println(fact(10))
	}

	// 4
	{
		import scala.annotation.tailrec

		def fact(n: BigInt): BigInt = (BigInt(1) to n).foldLeft(BigInt(1))(_*_)
		def fact2(n: BigInt): BigInt = (BigInt(1) /: (BigInt(1) to n)) (_*_)
		@tailrec def fact3(n: BigInt, acc: BigInt = 1): BigInt = if (n < 1) 1 * acc else fact3(n - 1, n * acc)
		println(fact(10) + " = " + fact2(10) + " = " + fact3(10))
		println(fact(0) + " = " + fact2(0) + " = " + fact3(0))
		println(fact(-1) + " = " + fact2(-1) + " = " + fact3(-1))
		println(fact(10000) + " = " + fact2(10000) + " = " + fact3(10000))
	}

	// 5
	{
		def largest(fun: (Int) => Int, inputs: Seq[Int]): Int = {
			inputs.map(fun(_)).max
		}
		println(largest(x => 10 * x - x * x, 1 to 10))
	}

	// 6
	{
		def largestAt(fun: (Int) => Int, inputs: Seq[Int]): Int = {
			inputs.map(x => (x, fun(x))).reduceLeft((x, y) => if (x._2 >= y._2) x else y)._1
		}
		println(largestAt(x => 10 * x - x * x, 1 to 10))
	}

	// 7
	def adjustToPair[A, B, C](fun: (A, B) => C)(pair: (A, B)): C = fun(pair._1, pair._2)

	{
		val pairs = (1 to 10) zip (11 to 20)
		println(pairs.map(adjustToPair(_ + _)))
		println(adjustToPair[Int, Int, Int](_ * _)((6, 7)))
	}

	// 8
	{
		println("a ab abc abcd".split(" ").corresponds(1 to 4)(_.length == _))
	}

	// 9
	{
		def corresponds2[A, B](a: Seq[A], b: Seq[B], fun: (A, B) => Boolean): Boolean =
			(a zip b).map(adjustToPair(fun(_,_))).reduceLeft(_ && _)
		println(corresponds2[String, Int]("a ab abc abcd".split(" "), 1 to 4, { _.length == _ }))
		// no type inference is possible here + too many parameters
	}

	// 10
	{
		def unless(condition: => Boolean)(block: => Unit): Unit = {
			println("In unless")
			if (!condition) { block }
		}
		unless (1 == 5) { println("1 != 5") }
		// Yes, it's a call-by-name parameter because until is not execute when we call it
		unless(1 == 5)_ // This shouldn't print 'In unless'
		// There is currying
		unless(1 == 5)(println("1 != 5"))
	}
}
