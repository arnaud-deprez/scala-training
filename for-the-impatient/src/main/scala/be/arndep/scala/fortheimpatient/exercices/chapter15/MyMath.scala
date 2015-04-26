package be.arndep.scala.fortheimpatient.exercices.chapter15

import scala.annotation.varargs

/**
 * Created by Arnaud on 04-05-15.
 */
class MyMath {
	@varargs def sum(terms: Int*): Int = terms.sum
}
