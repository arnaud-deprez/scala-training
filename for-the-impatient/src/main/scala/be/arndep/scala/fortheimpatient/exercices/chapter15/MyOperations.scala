package be.arndep.scala.fortheimpatient.exercices.chapter15

import scala.annotation.varargs
import scala.io.Source

/**
 * Created by Arnaud on 04-05-15.
 */
object MyOperations {
	def getLines(file: String): String = Source.fromFile(file).mkString

	// write 9 polymorphism definitions of alldifferent (1 for each sub
	@varargs
	def allDifferent[@specialized T](items: T*): Boolean = items.size == items.distinct.size
}
