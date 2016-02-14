package be.arndep.scala.fortheimpatient.exercices

import java.awt.Point

import scala.annotation.tailrec
import scala.language.{implicitConversions, postfixOps}


/**
 * Created by Arnaud on 23-05-15.
 */
object Chapter21 extends App {
	// 1
	/* A -> B implicitly create a Tuple2[A, B](self, y)
	implicit final class ArrowAssoc[A](private val self: A) extends AnyVal {
		@inline def -> [B](y: B): Tuple2[A, B] = Tuple2(self, y)
		def →[B](y: B): Tuple2[A, B] = ->(y)
	}*/

	// 2
	class MyRichVal[@specialized T](val x: T) {
		def +%(percent: Int): T =
			math.round(this.x.asInstanceOf[Double] * (1 + percent / 100.0)).asInstanceOf[T]
	}
	implicit def val2MyRichVal[@specialized T](x: T) = new MyRichVal(x)
	println(120 +% 10)

	// 3
	// Another way is to use directly implicit class
	implicit final class Factorial(val n: Int) {
		def fact(n: Int): Int = {
			@tailrec
			def tailrecFact(n: Int, acc: Int): Int =
				if (n == 1) acc else if (n == 0) 1 else tailrecFact(n - 1, acc * n)
			tailrecFact(n, 1)
		}

		def ! = fact(this.n)  // Actually it's possible :-D
		//  def ¡ = fact(this.n)  // But this is an error :-o
	}
	println(5!)

	// 5
	object Fraction {
		def apply(numerator: Int, denominator: Int) = new Fraction(numerator, denominator)
	}

	class Fraction(val numerator: Int, val denominator: Int) {
		override val toString = numerator + "/" + denominator
	}

	class RichFraction(f: Fraction) extends Fraction(f.numerator, f.denominator) with Ordered[Fraction] {
		def compare(that: Fraction) =
			(numerator.asInstanceOf[Double] / denominator).asInstanceOf[Int] -
				(that.numerator.asInstanceOf[Double] / that.denominator).asInstanceOf[Int]
	}
	implicit def fraction2RichFraction(x: Fraction) = new RichFraction(x)
	def smaller[T](a: T, b: T)(implicit ordered: T => Ordered[T]) =	if (a < b) a else b
	println( smaller(Fraction(1, 7), Fraction(2, 9)) )

	// 6
	class RichPointLex(p: Point) extends Point(p.getX.asInstanceOf[Int], p.getY.asInstanceOf[Int]) with Ordered[Point] {
		override def toString = (x, y).toString
		def compare(that: Point) = toString.compare((new RichPointLex(that)).toString)
	}
	object PointConversion {
		implicit def point2RichPointLex(point: java.awt.Point) = new RichPointLex(point)
		implicit def point2Double(point: java.awt.Point): Double = math.sqrt(point.x * point.x + point.y * point.y)
	}
	// In order to test comment point2Double from the package object.
	{
		import PointConversion.point2RichPointLex
		val p1 = new Point(1, 3)
		val p2 = new Point(1, 2)
		val p3 = new Point(2, 1)
		println(p1 > p2)
		println(p1 < p3)
		println(p2 > p3)
	}

	// 7
	{
		import PointConversion.point2Double
		val p1 = new Point(1, 3)
		val p2 = new Point(1, 2)
		val p3 = new Point(2, 1)
		println( p1 > p2 )
		println( p1 < p3 )
		println( p2 > p3 )
		println( p2 < p3 )
	}

	// 8 do it

	// 9
	/*
	  @implicitNotFound(msg = "Cannot prove that ${From} =:= ${To}.")
	  sealed abstract class =:=[From, To] extends (From => To) with Serializable // define abstract method apply.
	  private[this] final val singleton_=:= = new =:=[Any,Any] { def apply(x: Any): Any = x } // return itself so From equals To
	  object =:= {
	  	 //provide implicit conversion from A to A =:= A (= =:=[A,A])
		 implicit def tpEquals[A]: A =:= A = singleton_=:=.asInstanceOf[A =:= A]
	  }
	 */

	// 10
	println("abc".map(_.toUpper)) //buffer of char = String
	println("abc".map(_.toInt)) //buffer of int = Vector[Int]
}
