package be.arndep.scala.fortheimpatient.exercices

import scala.collection.mutable.ArrayBuffer

/**
  * Created by Arnaud on 15-05-15.
  */
object Chapter18 extends App {
	 // 1
	 {
		 class Bug {
			 private var pos = 0
			 private var direc = 1

			 def show(): this.type = { println(pos); this }
			 def move(steps: Int): this.type = { pos += steps * direc; this }
			 def turn(): this.type = { direc *= -1; this }
		 }
		 val bugsy = new Bug
		 bugsy.move(4).show().move(6).show().turn().move(5).show()
	 }

	 // 2
	 {
		 object Show
		 object Then
		 object Around

		 class Bug {
			 private var pos = 0
			 private var direc = 1


			 def show(): this.type = { println(pos); this }
			 def move(steps: Int): this.type = { pos += steps * direc; this }
			 def turn(): this.type = { direc *= -1; this }

			 def and(showLah: Show.type): this.type = show()
			 def and(thenLah: Then.type): this.type = this
			 def turn(aroundLah: Around.type): this.type = turn()
		 }
		 val bugsy = new Bug
		 // fluent API with singleton type
		 bugsy move 4 and Show and Then move 6 and Show turn Around move 5 and Show
	 }

	 // 3
	 {
		 class DocumentProperty
		 object Title extends DocumentProperty
		 object Author extends DocumentProperty

		 class Document {
			 private var useNextArgAs: Any = null
			 var title = ""
			 var author = ""

			 def set(obj: DocumentProperty): this.type = { useNextArgAs = obj; this }
			 def to(arg: String): this.type = {
				 useNextArgAs match {
					 case next: Title.type => title = arg
					 case next: Author.type => author = arg
				 }
				 this
			 }

			 override def toString = "\"" + title + "\", " + author
		 }
		 val book = new Document
		 println(book set Title to "Scala for the Impatient" set Author to "Cay Horstmann")
	 }

	 // 4
	 {
		 class Network {
			 class Member(val name: String) {
				 override def equals(that: Any) = that match {
					 case t: Member => true
					 case _ => false
				 }
				 override val toString = name
			 }

			 protected val members = new ArrayBuffer[Member]

			 def join(name: String) = {
				 val m = new Member(name)
				 members += m
				 m
			 }
		 }
		 val chat = new Network
		 val facebook = new Network
		 val fred = chat.join("Fred")
		 val john = chat.join("John")
		 val james = facebook.join("James")
		 println(fred.equals(john) + " != " + fred.equals(james))
	 }

	 // 5
	 {
		 object Network {
			 type NetworkMember = n.Member forSome { val n: Network }

			 def process[M <: n.Member forSome { val n: Network }](m1: M, m2: M) = (m1, m2) // section 18.8, here we can only use this for member of the same network
			 def process2[M <: Network#Member](m1: M, m2: M) = (m1, m2) // same as above but with type projection. With type projection we can do that with member from different network

			 def process3(m1: NetworkMember, m2: NetworkMember) = (m1, m2) // Here we use an alias for all Member class inside Network so it's equivalent from process2
		 }

		 class Network {
			 class Member(val name: String) {
				 override def equals(that: Any) = that match {
					 case t: Member => true
					 case _ => false
				 }
				 override val toString = name
			 }

			 protected val members = new ArrayBuffer[Member]

			 def join(name: String) = {
				 val m = new Member(name)
				 members += m
				 m
			 }
		 }

		 val chat = new Network
		 val facebook = new Network
		 val fred = chat.join("Fred")
		 val john = chat.join("John")
		 val james = facebook.join("James")

		 println(Network.process(fred, john))
		 println(Network.process2(fred, james))
		 println(Network.process3(fred, james))
	 }

	 // 6
	 {
		 //use infix notation Eitheir[Int, Int] = Int Either Int
		 def findIndex(array: Array[Int], n: Int): Int Either Int = {
			 // Divide and Conquer
			 def findIndexDC(array: Array[Int], start: Int, end: Int, n: Int): Int Either Int = {
				 if (array.isEmpty)
					 throw new IllegalArgumentException
				 if (end - start == 0) {
					 if (array(start) == n) Right(array(start)) else Left(array(start))
				 }
				 else {
					 val l = findIndexDC(array, start, start + (end-start) / 2, n)
					 val r = findIndexDC(array, start + (end-start) / 2 + 1, end, n)
					 //        println("l: " + l)
					 //        println("r: " + r)

					 if (l.isInstanceOf[Left[_, _]] && r.isInstanceOf[Left[_, _]]) {
						 val dl = math.abs(l.left.get - n)
						 val dr = math.abs(r.left.get - n)
						 if (dl < dr) l else if (dl == dr) l else r
					 }
					 else if (l.isInstanceOf[Left[_, _]]) {
						 r
					 }
					 else if (r.isInstanceOf[Left[_, _]]) {
						 l
					 }
					 else throw new Error("Cannot, lah!")
				 }
			 }

			 findIndexDC(array, 0, array.size - 1, n)
		 }

		 println(findIndex(Array(1, 2, 4, 5), 1))
		 println(findIndex(Array(1, 2, 4, 5), 3))
		 println(findIndex(Array(1), 3))
	 }

	 // 7
	 {
		 def process(closeable: { def close(): Unit }) {
			 def doSomething(obj: Any) {
				 println("Calling toString on the object: " + obj)
			 }

			 try doSomething(closeable)
			 finally closeable.close()
		 }

		 class MyCloseable {
			 def close() { println("close") }
			 override def toString: String = classOf[MyCloseable].toGenericString
		 }

		 val c = new MyCloseable
		 process(c)
	 }

	 // 8
	 {
		 def printValues(f: { def apply(n: Int): Int }, from: Int, to: Int) {
			 (from to to).foreach((n: Int) => print(f(n) + " "))
			 println()
		 }
		 printValues((x: Int) => x * x, 3, 6) // Prints 9 16 25 36
		 printValues(Array(1, 1, 2, 3, 5, 8, 13, 21, 34, 55), 3, 6) // Prints 3 5 8 13
	 }

	 // 9
	 {
		 abstract class Dim[T](val value: Double, val name: String) {
			 protected def create(v: Double): T
			 def +(other: this.type) = create(value + other.value) // With self type (this.type), we can only add 2 Dim of same unit
			 override def toString() = value + " " + name
		 }

		 class Seconds(v: Double) extends Dim[Seconds](v, "s") {
			 override def create(v: Double) = new Seconds(v)
		 }

		 class Meters(v: Double) extends Dim[Seconds](v, "m") {
			 override def create(v: Double) = new Seconds(v)
		 }

		 val s = new Seconds(3)
		 val m = new Meters(5)
		 println(s + s)
 //		println(s + m) // doesn't compile
	 }

	 // 10
	 {
		 class C {
			 println("Init C")
			 def f {
				 println("C.f")
			 }
		 }

		 class D extends C {
			 println("Init D")
		 }

		 trait X1 {
			 this: C =>
			 println("Init X1")

			 override def f {
				 println("X1.f")
			 }
		 }

		 trait X2 extends C {
			 println("Init X2")

			 override def f {
				 super.f
				 println("X2.f")
			 }
		 }

		 trait X31 extends C with X1 {
			 println("Init X31")

			 override def f {
				 super.f
				 println("X31.f")
			 }
		 }

		 trait X32 extends X2 {
			 println("Init X32")

			 override def f {
				 super.f
				 println("X32.f")
			 }
		 }

		 (new D with X31).f //does not print C.f
		 println
		 (new D with X32).f //print C.f
		 println
	 }
 }
