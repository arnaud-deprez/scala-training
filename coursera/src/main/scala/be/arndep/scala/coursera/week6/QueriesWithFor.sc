case class Book(title: String, authors: List[String])

val books = Set(
	Book(
		title = "Structure and Interpretation of Computer Programs",
		authors = List("Abelson, Harald", "Sussman, Gerald J.")
	),
	Book(
		title = "Introduction to Functional Programming",
		authors = List("Bird, Richard", "Wadler, Phil")
	),
	Book(
		title = "Effective Java",
		authors = List("Bloch, Joshua")
	),
	Book(
		title = "Effective Java 2",
		authors = List("Bloch, Joshua")
	),
	Book(
		title = "Java Puzzlers",
		authors = List("Bloch, Joshua", "Gafter, Neal")
	),
	Book(
		title = "Programming in Scala",
		authors = List("Odersky, Martin", "Spoon, Lex", "Venners, Bill")
	)
)

//Find titles of books whose author's name is Bird
for (b <- books; a <- b.authors if a.startsWith("Bird,"))
	yield b.title

//Find all books which have the work "Program" in title
for (b <- books if b.title.contains("Program"))
	yield b.title

//Find the names of all authors who have written at least two books
for {
	b1 <- books
	b2 <- books
	if b1.title < b2.title
	a1 <- b1.authors
	a2 <- b2.authors
	if a1 == a2
} yield a1