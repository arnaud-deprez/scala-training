import scala.io.Source

val in = Source.fromURL("https://raw.githubusercontent.com/frankh/coursera-scala/master/forcomp/src/main/resources/forcomp/linuxwords.txt")
val words = in.getLines.toList filter(word => word forall(_.isLetter))

val nmem = Map('2' -> "ABC", '3' -> "DEF", '4' -> "GHI", '5' -> "JKL", '6' -> "MNO",
	'7' -> "PQRS", '8' -> "TUV", '9' -> "WXYZ")
//Invert the nmem to give map of char 'A' ... 'Z' to digit '2' ... '9'
val charCode: Map[Char, Char] = nmem.flatMap(t => t._2.map(_ -> t._1))
//Map a word to the digit string that it represents. e.g. "Java" -> "5282"
def wordCode(word: String): String = word.toUpperCase map charCode //Awesome, a Map is a (partial) function !
wordCode("Java")
	/**
	 * A map from digit strings to the words that represent them.
	 * e.g. "5282" -> ("Java", "Kata", "Lava", ...)
	 * Note: a missing number should map to the empty Set. e.g. "1111" -> List()
	 */
val wordsForNum: Map[String, Seq[String]] = words groupBy wordCode withDefaultValue Seq()

//Return all ways to encode a number as a list of words
def encode(number: String): Set[List[String]] =
	if (number.isEmpty) Set(List())
	else {
		for {
			split <- 1 to number.length
			word <- wordsForNum(number take split)
			rest <- encode(number drop split)
		} yield word :: rest
	}.toSet
encode("7225247386")

def translate(number: String): Set[String] = encode(number).map(_ mkString " ")
translate("7225247386")
