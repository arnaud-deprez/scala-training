package be.arndep.scala.fortheimpatient

import _root_.scala.math.abs

/**
 * Created by Arnaud on 02-05-15.
 */
package object chapter11_operator {
	def sign(x: Int) = if (x > 0) 1 else if (x < 0) -1 else 0
	def gcd(x: Int, y: Int): Int = if (y == 0) abs(x) else gcd(y, x % y)
	def lcm(x: Int, y: Int) = abs(x * y) / gcd(x, y)
}
