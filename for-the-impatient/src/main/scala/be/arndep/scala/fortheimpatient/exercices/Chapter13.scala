package be.arndep.scala.fortheimpatient.exercices

/**
 * Created by Arnaud on 03-05-15.
 */
object Chapter13 extends App {
	// 1
	{
		import scala.collection.mutable._
		def indexes(str:String) = {
			val result = Map[Char, TreeSet[Int]]()
			str.zipWithIndex.foreach((ci: (Char, Int)) =>
				result(ci._1) = result.getOrElse(ci._1, TreeSet[Int]()) + ci._2)
			result
		}
		println(indexes("Mississippi"))
	}

	// 2
	{
		def indexes(str:String) = {
			//Took from another guy and I don't get it : _*
//			Map(str.distinct.map(c => c -> List(str.zipWithIndex.filter(c == _._1).map(_._2): _*)): _*)
			str.zipWithIndex.groupBy(_._1).map(x => (x._1, x._2.map(_._2).toList)) //zipWithIndex (c -> v) groupBy (c -> Vector((c -> i)*)
			// other way
			//str.zipWithIndex.foldLeft(Map[Char, List[Int]]())((m, t) => m + (t._1 -> (m.getOrElse(t._1, List[Int]()) :+ t._2)))
		}
		println(indexes("Mississippi"))
	}

	// 3
	{
		// easy way in filterNot(_ == 0)
		def filterZero(lst: List[Int]): List[Int] = lst match {
			case Nil => Nil
			case h :: t => if (h != 0) h :: filterZero(t) else filterZero(t)
		}
		println(filterZero(List(0, 1, 2, 0, 3, 4)))
	}

	// 4
	{
		def existIn(a: Array[String], m: Map[String, Int]): Array[Int] = a.map(m.get(_)).flatMap(x => x)
		val a = Array("Tom", "Fred", "Harry")
		val m = Map("Tom" -> 3, "Dick" -> 4, "Harry" -> 5)
		val x = existIn(a, m)
		println(x mkString)
	}

	// 5
	{
		// use currying
		def mkString[T](start: String = "", sep: String = ", ", end: String = ""): (Seq[String]) => String = {
			s: Seq[String] => start + s.map(_.toString).reduceLeft(_ + sep + _) + end
		}
		println(mkString(start = "[", end = "]")(Array("Tom", "Fred", "Harry")))
	}

	// 6
	{
		val lst = List(1, 2, 3, 4, 5)
		println(lst)
		val x = (lst :\ List[Int](6, 7))(_ :: _) // foldRight + prepend element from right to left
		println("x = " + x)
		println("x = " + lst.foldRight(List[Int](6, 7))(_ :: _))
		val y = (List[Int](6, 7) /: lst)(_ :+ _) // foldLeft + //append element from left to right
		println("y = " + y)
		println("y = " + lst.foldLeft(List[Int](6, 7))(_ :+ _))
		val z = (List[Int](6, 7) /: lst)((x,y) => y :: x) //foldLeft + prepend element from left to right
		println("z = " + z)
		println("z = " + lst.foldLeft(List[Int](6, 7))((x, y) => y :: x))
	}

	// 7
	{
		// (prices zip quantities) map { p => p._1 * p._2 }
		val prices = List(5.0, 20.0, 9.95)
		val quantities = List(10, 2, 1)
		println((prices zip quantities) map {Function.tupled(_*_)})
		//another way
		println((prices, quantities).zipped map { _ * _ })
	}

	// 8
	{
		def arrayDim(a: Array[Int], numOfDim: Int) = a.grouped(numOfDim).toArray.map(_.toArray)

		val a = (1 to 9).toArray
		val x = arrayDim(a, 3)

		println(x.deep.mkString(", "))
	}

	// 9
	{
		/*
		It can go wrong and reset a value to 0
		val frequencies = new scala.collection.mutable.HashMap[Char, Int] with scala.collection.mutable.SynchronizedMap[Char, Int]
		frequencies(c) = frequencies.getOrElse(c, 0) + 1
		*/

		/*
		It's a better idea to use concurrent tool from java
		import scala.collection.JavaConversions.mapAsScalaConcurrentMap
		val frequencies: scala.collection.mutable.Map[Char, Int] = mapAsScalaConcurrentMap(new java.util.concurrent.ConcurrentHashMap[Char, Int])
		*/
	}

	// 10
	{
		val str = io.Source.fromFile("files/license.txt").mkString

		def printMills(msg: String)(block: => Unit) {
			val start = System.currentTimeMillis()
			block
			val end = System.currentTimeMillis()
			println(msg.format(end-start))
		}

		printMills("Using mutable collection: %d ms") {
			val freq = new collection.mutable.HashMap[Char, Int]
			for (c <- str) freq(c) = freq.getOrElse(c, 0) + 1
			println(freq.toSeq.sorted)
		}


		printMills("Using immutable collection: %d ms") {
			val freq = str.map(c => (c, 1)).groupBy(_._1).map(x => (x._1, x._2.length))
			println(freq.seq.toSeq.sorted)
		}

		//Working with mutable object is not a good idea when we want to parallelize things
		printMills("Using mutable parallel collection: %d ms") {
			val freq = str.par.aggregate(collection.mutable.Map[Char, Int]())(
				(x, c) => x += (c -> (x.getOrElse(c, 0) + 1)),
				(map1, map2) => map1 ++ map2.map(t => (t._1 -> (map1.getOrElse(t._1, 0) + t._2)))
			)
			println(freq.toSeq.sorted)
		}

		printMills("Using immutable parallel collection: %d ms") {
			val freq = str.par.aggregate(collection.immutable.Map[Char, Int]())(
				(x, c) => x + (c -> (x.getOrElse(c, 0) + 1)),
				(map1, map2) => map1 ++ map2.map(t => (t._1 -> (map1.getOrElse(t._1, 0) + t._2)))
			)
			println(freq.toSeq.sorted)
		}
	}
}
