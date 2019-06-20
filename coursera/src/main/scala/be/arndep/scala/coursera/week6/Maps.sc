class Poly(terms0: Map[Int, Double]) {
	def this(bindings: (Int, Double)*) = this(bindings.toMap)

	val terms = terms0 withDefaultValue 0.0

	//v1
	/*def +(other: Poly): Poly = new Poly(terms ++ (other.terms map adjust))
	private def adjust(term: (Int, Double)): (Int, Double) = {
		val (exp, coeff) = term
		exp -> (coeff + terms(exp))
	}*/
	//v2
	def +(other: Poly) =
		new Poly((other.terms foldLeft terms)(addTerm))

	def addTerm(terms: Map[Int, Double], term: (Int, Double)) = {
		val (exp, coeff) = term
		terms + (exp -> (coeff + terms(exp)))
	}

	override def toString = terms.toList.sorted.reverse.map(t => t._2 + "x^" + t._1) mkString " + "
}

val p1 = new Poly(1 -> 2.0, 3 -> 4.0, 5 -> 6.2)
val p2 = new Poly(Map(0 -> 3.0, 3 -> 7.0))
//6.2x^5 + 11.0x^3 + 2.0x^1 + 3.0x^0
p1 + p2
//Default values
p1.terms(7)