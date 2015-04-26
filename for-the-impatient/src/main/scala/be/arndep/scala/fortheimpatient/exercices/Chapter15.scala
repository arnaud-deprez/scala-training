package be.arndep.scala.fortheimpatient.exercices

import be.arndep.scala.fortheimpatient.exercices.chapter15.MyMath

import scala.annotation.tailrec

/**
 * Created by arnaud on 04/05/15.
 */
object Chapter15 extends App {
	// 1 Take a look at MoneyTest

	// 2
	{
		@deprecated
		class AnnotationsPositions[@deprecated T] @deprecated() (x: T) {
			@deprecated
			var field: AnyRef @deprecated = null

			@deprecated
			type S = T

			@deprecated
			def method(@deprecated @deprecatedName('argument) arg: Any): Any = {
				@deprecated
				var local: Any = null

				(local: @deprecated) match {
					case x: String => "It's a String :-D"
					case x: Any => "Don't know what's this :-("
				}
			}
		}
	}

	// 3 @deprecated ?

	// 4
	{
		println(new MyMath().sum(1, 2, 3, 4, 5, 6)) //look at ScalaCaller
	}

	// 5 look at ScalaCaller

	// 6
	{
		object Volatile {
			@volatile var bool: Boolean = false
		}

		object setterTask extends Runnable {
			override def run() {
				Thread.sleep(5000)
				Volatile.bool = true
			}
		}

		object checkerTask extends Runnable {
			override def run() {
				while (!Volatile.bool) {
					Thread.sleep(100)
				}
				println("Field's value is now true.")
			}
		}

		val checkerThread = new Thread(checkerTask, "CHECKER")
		val setterThread = new Thread(setterTask, "SETTER")

		print("Starting checker thread... ")
		checkerThread.start(); println("READY")
		print("Starting setter thread... ")
		setterThread.start(); println("READY")
	}

	// 7
	{
		class MyMath {
			// If the method is not final, it can't compile with @tailrec
			// How can we perform a loop instead of recursion if we can override that behavior ?
			@tailrec final def factorial(n: BigInt, acc: BigInt = 1): BigInt = {
				if (n < 1) 1 * acc else factorial(n - 1, n * acc)
			}
		}
	}

	// 8 MyOperations

	// 9

	// 10
	{
		def factorial(n: Int): Int = {
			assert(n >= 0, "whole number parameter") // The code can be compiled without assertions
			if (n < 0) throw new Error("The argument needs to be a whole number.")
			if (n == 1) 1 else if (n == 0) 1 else factorial(n - 1) * n
		}
	}
}
