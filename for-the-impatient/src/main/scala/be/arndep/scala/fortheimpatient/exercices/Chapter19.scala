package be.arndep.scala.fortheimpatient.exercices

import java.util.{Calendar, Date}

import be.arndep.scala.fortheimpatient.exercices.chapter19.MathExpressionParser

import scala.util.parsing.combinator.RegexParsers

/**
 * Created by Arnaud on 16-05-15.
 */
object Chapter19 extends App {
	def printResult[T](r: MathExpressionParser#ParseResult[T]) { if (r.successful) println(r.get) else println("Error: " + r + " first = " + r.next.first)}
	// 1 add / and %
	{
		val parser = new MathExpressionParser
		printResult(parser.parseAll(parser.parse, "3-4*(5-2)")) // = -9
		printResult(parser.parseAll(parser.parse, "3-(4-5)")) // = 4
		printResult(parser.parseAll(parser.parse, "3")) // = 4
		printResult(parser.parseAll(parser.parse, "3 * 2")) // = 4
		printResult(parser.parseAll(parser.parse, "3 - 4/5 - 0.2")) // = 2
		printResult(parser.parseAll(parser.parse, "3 % 2")) // = 1
	}

	// 2 add ^ (pow)
	{
		val parser = new MathExpressionParser
		printResult(parser.parseAll(parser.parse, "4^2^3")) // = -9
	}

	// 3
	{
		class ListParser extends RegexParsers {
			def expr: Parser[List[Int]] = repsep(element, ",")
			private def element: Parser[Int] = """-?\d+""".r ^^ { _.toInt }
		}
		val parser = new ListParser
		val r = parser.parseAll(parser.expr, "1, 23, -79")
		if (r.successful) println(r.get)
	}

	// 4
	{
		class ISODateParser extends RegexParsers {
			val number = "[0-9]+".r

			val hyphen = "-"
			val cln = ":"

			def expr: Parser[Date] = date ~ ("T" ~> time) ^^ {
				case d ~ t =>
					t.set(d.get(Calendar.YEAR), d.get(Calendar.MONTH), d.get(Calendar.DATE))
					t.getTime
			}

			def date: Parser[Calendar] = (number <~ hyphen) ~ (number <~ hyphen) ~ number ^^ {
				case y ~ m ~ d =>
					val cal: Calendar = Calendar.getInstance
					cal.set(y.toInt, m.toInt, d.toInt)
					cal
			}

			def time: Parser[Calendar] = (number <~ cln) ~ (number <~ cln) ~ number ^^ {
				case h ~ m ~ s =>
					val cal: Calendar = Calendar.getInstance
					cal.set(Calendar.HOUR_OF_DAY, h.toInt)
					cal.set(Calendar.MINUTE, m.toInt)
					cal.set(Calendar.SECOND, s.toInt)
					cal
			}
		}
		val parser = new ISODateParser
		val r = parser.parseAll(parser.expr, "2015-05-16T17:35:42")
		if (r.successful) println(r.get) else println("Error: " + r)
	}

	// 5
	{

	}

	// 6
	{
		// Use Substraction instead of Operator("-", left, right). No need to use pattern matching when it's not necessary
		val parser = new MathExpressionParser
		printResult(parser.parseAll(parser.parse, "3-4-5")) // = -6
	}

	// 7
	{
		class ExprParser extends RegexParsers {
			private val number = """\d+(\.\d+)*""".r

			def expr: Parser[Double] = term ~ rep(("+" | "-") ~ term) ^^ {
				//Use foldLeft ((t0 ± t1) ± t2) ± . . .
				case t ~ r => (t /: r)((a, b) => b._1 match {
					case "+" => a + b._2
					case "-" => a - b._2
				})
			}

			def term: Parser[Double] = number ^^ { _.toDouble } | "(" ~> expr <~ ")"
		}
		val parser = new ExprParser()
		val result = parser.parseAll(parser.expr, "3-4 -5")
		if (result.successful) println(result.get)
	}

	// 8
	{
		val parser = new MathExpressionParser
		printResult(parser.parseAll(parser.parse, "a = 4;"))
		printResult(parser.parseAll(parser.parse, "{ a = 4; b; out = a;}"))
	}

	// 9
	{
		val parser = new MathExpressionParser
		printResult(parser.parseAll(parser.parse, "while(i < 5) i+1"))
		printResult(parser.parseAll(parser.parse, "if(i < 5) i+1"))
		printResult(parser.parseAll(parser.parse, "if(i < 5) i+1 else i+2"))
	}
}
