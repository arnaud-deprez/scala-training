package be.arndep.scala.fortheimpatient.exercices.chapter19

import scala.util.parsing.combinator.{JavaTokenParsers, PackratParsers}

/**
 * Created by Arnaud on 16-05-15.
 */
abstract class Expr
abstract class BinaryExpr(left: Expr, right: Expr) extends Expr
case class Var(id: String) extends Expr
case class Number(value: Double) extends Expr
case class Bool(value: Boolean) extends Expr
case class Sum(left: Expr, right: Expr) extends BinaryExpr(left, right)
case class Substraction(left: Expr, right: Expr) extends BinaryExpr(left, right)
case class Product(left: Expr, right: Expr) extends BinaryExpr(left, right)
case class Division(left: Expr, right: Expr) extends BinaryExpr(left, right)
case class Remainder(left: Expr, right: Expr) extends BinaryExpr(left, right)
case class Pow(left: Expr, right: Expr) extends BinaryExpr(left, right)
case class BooleanExpr(comp: String, left: Expr, right: Expr) extends BinaryExpr(left, right)

abstract class Stmt
case class Seqn(s: Seq[Stmt]) extends Stmt
case class Asign (v : Option[String], e : Expr) extends Stmt
case class IfElse (e : Expr, bTrue : Stmt, bFalse: Option[Stmt]) extends Stmt
case class While (e : Expr, b : Stmt) extends Stmt
case class Null () extends Stmt

/**
 * By default RegexParsers skip whitespaces!
 */
class MathExpressionParser extends JavaTokenParsers with PackratParsers {

	lazy val parse: PackratParser[Stmt] = phrase(stmt)
	private lazy val stmt: PackratParser[Stmt] = ";" ^^^ Null() | sequenceStmt | assignmentStmt | whileStmt | ifElseStmt | failure("Not a valid stmt")
	private lazy val sequenceStmt: PackratParser[Stmt] = "{" ~> (stmt*) <~ "}" ^^ (s => Seqn(s))
	private lazy val assignmentStmt: PackratParser[Stmt] = idn ~! opt("=" ~> expr) <~ ";" ^^ (t => Asign(Some(t._1), t._2.getOrElse(Number(0)))) | expr ^^ (e => Asign(None, e))
	private lazy val whileStmt: PackratParser[Stmt] = ("while" ~> "(" ~> boolExpr <~ ")") ~! stmt ^^ (t => While(t._1, t._2))
	private lazy val ifElseStmt: PackratParser[Stmt] = ("if" ~> "(" ~> boolExpr <~ ")") ~! stmt ~! opt("else" ~> stmt) ^^ (t => IfElse(t._1._1, t._1._2, t._2))

	lazy val boolExpr: PackratParser[Expr] = boolExpr ~! ("&&" | "|") ~! boolExpr ^^ {
		case e1 ~ op ~ e2 => BooleanExpr(op, e1, e2)
	} | expr ~! (">" | ">=" | "<" | "<=" | "==" | "!=") ~! expr ^^ {
		case e1 ~ op ~ e2 => BooleanExpr(op, e1, e2)
	} | ("true" | "false") ^^ {
		case s => BooleanExpr("==", Bool(s == "true"), Bool(true))
	}

	// PackratParsers allow us to use left recursion ! Awesome !
	lazy val expr: PackratParser[Expr] = expr ~! opt(("+" | "-") ~! termExpr) ^^ {
		case t ~ None => t
		case t ~ Some("+" ~ e) => Sum(t, e)
		case t ~ Some("-" ~ e) => Substraction(t, e)
	} | termExpr

	private lazy val termExpr: PackratParser[Expr] = powExpr ~! rep(("*" | "/" | "%") ~! powExpr) ^^ {
		case a ~ List() => a
		case a ~ List("*" ~ f) => Product(a, f)
		case a ~ List("/" ~ f) => Division(a, f)
		case a ~ List("%" ~ f) => Remainder(a, f)
	}

	private lazy val powExpr: PackratParser[Expr] = factor ~! opt(("^") ~> powExpr) ^^ {
		case p ~ None => p
		case p ~ Some(f) => Pow(p, f)
	}

	private lazy val factor: PackratParser[Expr] = decimalNumber ^^ (n => Number(n.toDouble)) | idn ^^ (s => Var(s)) | "(" ~> expr <~ ")" | failure("Not a valid factorExpr")

	lazy val idn : PackratParser[String] = not(keyword) ~> "[a-zA-Z][a-zA-Z0-9]*".r
	lazy val keyword : Parser[String] = ("while" | "if" | "else" | "true" | "false")
}

object Test extends App {
	def printResult[T](r: MathExpressionParser#ParseResult[T]) { if (r.successful) println(r.get) else println("Error: " + r)}

	val parser = new MathExpressionParser
	printResult(parser.parseAll(parser.parse, "a = 4;"))
//	printResult(parser.parseAll(parser.parse, "{ a = 4; b; out = a}"))
	printResult(parser.parseAll(parser.parse, "while(i<5) i+1"))
}