package be.arndep.scala.fortheimpatient.exercices

import scala.language.postfixOps

/**
 * Created by arnaud on 26/04/15.
 */
object Chapter4 extends App {
	// 1
	{
		val wishbeer: Map[String, Double] = Map(
			"Elephantine Braille Input Device" -> 322000,
			"Microsoft Word 95" -> 10,
			"4TB Ram Drive" -> 32000,
			"Apple" -> 0.60)
		println(wishbeer)
		println(for ((k, v) <- wishbeer) yield k -> v * 0.9)
		println(wishbeer.map(tuple => tuple._1 -> tuple._2 * 0.9))
	}

	// 2
	{
		val in = new java.util.Scanner(java.nio.file.Paths.get("files/scanned-file.txt"))
		val wordsCount = collection.mutable.Map[String, Int]() withDefault (_ => 0) //awesome !
		while (in hasNext) wordsCount(in next) += 1
		println(wordsCount)
	}

	// 3
	{
		val in = new java.util.Scanner(java.nio.file.Paths.get("files/scanned-file.txt"))
		var wordsCount = Map[String, Int]() withDefault (_ => 0) //awesome !
		while (in hasNext) {
			val key = in.next
			val currentValue = wordsCount(key)
			wordsCount = wordsCount + (key -> (currentValue + 1))
		}
		println(wordsCount)
	}

	// 4
	{
		val in = new java.util.Scanner(java.nio.file.Paths.get("files/scanned-file.txt"))
		var wordsCount = collection.immutable.SortedMap[String, Int]()
		while (in hasNext) {
			val key = in.next
			val currentValue = wordsCount.getOrElse(key, 0) //awesome too!
			wordsCount = wordsCount + (key -> (currentValue + 1))
		}
		println(wordsCount)
	}

	// 5
	{
		val in = new java.util.Scanner(java.nio.file.Paths.get("files/scanned-file.txt"))
		val wordsCount = new java.util.TreeMap[String, Int]()
		while (in hasNext) {
			val key = in.next
			val currentValue = wordsCount.getOrDefault(key, 0) // awesome from java8!
			wordsCount.put(key, (currentValue + 1))
		}
		println(wordsCount)
	}

	// 6
	{
		import java.util.Calendar._
		val stringsToCalConst = collection.mutable.LinkedHashMap(
			"Monday" -> MONDAY,
			"Tuesday" -> TUESDAY,
			"Wednesday" -> WEDNESDAY,
			"Thursday" -> THURSDAY,
			"Friday" -> FRIDAY,
			"Saturday" -> SATURDAY,
			"Sunday" -> SUNDAY)
		println(stringsToCalConst)
	}

	// 7
	{
		val props = collection.JavaConversions.propertiesAsScalaMap(System getProperties)
		val maxLengthKey = (props.keySet.toList.maxBy(_.size))
		for ((k, v) <- props) {
			println(k + " " * (maxLengthKey.size - k.size) + " | " + v)
		}
	}

	// 8
	val arr = Array(-5, 0, 5, 9, -2, 17, 3)

	def minmax(values: Array[Int]) = {
		(values.min, values.max)
	}

	println(minmax(arr))

	// 9
	def lteqgt(values: Array[Int], v: Int) = {
		(values.filter(_ < v).size, values.filter(_ == v).size, values.filter(_ > v).size) // All except tuple's parens could be eliminated with '.'
	}

	println(lteqgt(arr, 0))

	// 10
	println("Hello".zip("World"))
	// Encryption?

	// Allowing so many '.' to be omitted can easily become detrimental. If it is to be, it is up to me... D.A.R.E
}
