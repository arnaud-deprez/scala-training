package be.arndep.scala.fortheimpatient.exercices

/**
 * Created by Arnaud on 14-05-15.
 */
object Chapter17 extends App {
	// 1
	{
		class MyPair[T, S](val left: T, val right: S) {
			def swap(): MyPair[S, T] = new MyPair(right, left)
			override def toString: String = "(" + left + "," + right + ")"
		}
		println(new MyPair(1, "james").swap)
	}

	// 2
	{
		class MyPair[T](var left: T, var right: T) {
			def swap(): MyPair[T] = {
				val l = left
				left = right
				right = l
				this
			}
			override def toString: String = "(" + left + "," + right + ")"
		}
		println(new MyPair(1, 2).swap)
	}

	// 3
	{
		class MyPair[T, S](val left: T, val right: S) {
			def swap(): MyPair[S, T] = new MyPair(right, left)
			def swap[T, S](pair: MyPair[T, S]): MyPair[S, T] = new MyPair(pair.right, pair.left)
			override def toString: String = "(" + left + "," + right + ")"
		}
		val p = new MyPair("raoul", 3.5)
		println(p.swap(new MyPair(List(1, 2), "test")))
	}

	// 4 We can't use a lower bound with the method replace first in 17.3 because if we replace the first element element
	// with a subclass of Student, than we have a error because the second parameter of type Sutdent is not the new subtype in first parameter.
	// Instead we should use an upper bound because we can replace a Student by a Person a make a new Pair[Person]

	// 5 RichInt implements Comparable[Int] because scala provide an implicit conversion from Int to RichInt, that's why it shouldn't implement Comparable[RichInt]

	// 6
	{
		def mid[T](coll: Iterable[T]): Option[T] = {
			val midPos = if (coll.nonEmpty && coll.size % 2 == 1) math.floor(coll.size / 2D).toInt else return None
			Some(coll.drop(midPos).head)
		}
		println(mid("")) // None
		println(mid("ab")) // None because it has a peer size
		println(mid("World")) // Some("r")
	}

	// 7

	// 8 because an mutable Pair can't change its type to an upper type

	// 9
	{
		class NastyPair[+T](val first: T, val second: T) {
			def replaceFirst[R >: T](newFirst: R) = new NastyPair(newFirst, second)

			override val toString = (first, second).toString
		}

		class NastyDoublePair(firstC: Double, secondC: Double) extends NastyPair[Double](firstC, secondC) {
			override def replaceFirst[R >: Double](newFirst: R) = {
				newFirst match {
					case x: Double =>
						new NastyDoublePair(math.sqrt(x.asInstanceOf[Double]), second)
					case _ => new NastyDoublePair(Double.NaN, second) // If I call the super.replaceFirst, it works as expected
				}
			}
		}

		val p: NastyPair[Any] = new NastyDoublePair(4.0, 5.0)
		println(p.replaceFirst("Hello"))
	}

	// 10
	{
		class MyPair[S, T](var left: S, var right: T) {
			def swap()(implicit ev: S =:= T, ev2: T =:= S): MyPair[S, T] = {
				val tmp = left
				left = right
				right = tmp
				this
			}

			override def toString = (left, right).toString
		}

		println(new MyPair("left", "right").swap)
//		println(new MyPair("test", 2).swap) // can't compile because String != Int
	}
}
