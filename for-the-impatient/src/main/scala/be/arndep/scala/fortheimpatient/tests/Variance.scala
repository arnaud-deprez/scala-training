package be.arndep.scala.fortheimpatient.tests

import scala.collection.mutable.ArrayBuffer

/**
 * Created by Arnaud on 30-05-15.
 */
object Variance extends App{
	// Example from http://docs.scala-lang.org/tutorials/tour/variances.html
	/**
	 * Scala supports variance annotations of type parameters of generic classes. In contrast to Java 5 (aka. JDK 1.5),
	 * variance annotations may be added when a class abstraction is defined, whereas in Java 5,
	 * variance annotations are given by clients when a class abstraction is used.
	 */
	{
		/**
		 * This class must be covariant [+T] for the pop method because in the push method,
		 * the Stack[T] must be a subtype of the Stack[S]
		 * @tparam T
		 */
		class Stack[+T] {
			def push[S >: T](elem: S): Stack[S] = new Stack[S] {
				override def top: S = elem

				//Stack.this = this of outer instance
				override def pop: Stack[S] = Stack.this

				override def toString() = elem.toString() + " " + Stack.this.toString()
			}

			def top: T = sys.error("no element on stack")

			def pop: Stack[T] = sys.error("no element on stack")

			override def toString() = ""
		}

		val s: Stack[String] = new Stack().push("hello");
		val t: Stack[Any] = s.push(7).push(new Object).push(3.0);
		println(t + ", " + t.pop)
	}

	{
		class Person(val name: String, val age: Int);
		class Student(override val name: String, override val age: Int, school: String) extends Person(name, age);

		/**
		 * Example with contravariance
		 * @tparam A
		 */
		trait PrettyPrinter[-A] {
			def pprint(a: A): String
		}
		def pprint[A](a: A)(implicit p: PrettyPrinter[A]): String = p.pprint(a)

		implicit object PersonPrettyPrinter extends PrettyPrinter[Person] {
			def pprint(p: Person): String = "[Person : %s, %d]" format (p.name, p.age)
		}

		/**
		 * Need contravariance for currying and implicit conversion !
		 * Indeed, the definition of pprint requires a PrettyPrinter[A] for an object of type [A]
		 * We define a PersonPrettyPrinter to print a person and we didn't define a StudentPrettyPrinter.
		 * For that reason, when we try to print a Student which is a subtype of Person, we need to have the relation
		 * PrettyPrinter[Student] is a super type of PrettyPrinter[Person]
		 */
		println(pprint(new Person("Tom", 32))(PersonPrettyPrinter))
		println(pprint(new Student("James", 21, "HEPL"))(PersonPrettyPrinter)) // fails if not contravariance

		println(pprint(new Person("Tom", 32)))
		println(pprint(new Student("James", 21, "HEPL"))) // fails if not contravariance
	}

	{
		trait Friend[-T] {
			def befriend(someone: T)
		}

		class Person(val name: String, val age: Int) extends Serializable with Friend[Person] {
			private[Person] val friends: ArrayBuffer[Person] = ArrayBuffer()
			def befriend(friend: Person) = {
				friend.friends += this
				friends += friend
				this
			}
			override def toString = name + "(" + age + ", friends: " + friends.map(p => p.name) + ")"
		}

		class Student(override val name: String, override val age: Int, school: String) extends Person(name, age);

		def makeFriendWith(s: Student, f: Friend[Student]) { f.befriend(s) }
		val susan = new Student("Susan", 22, "HEPL")
		val fred = new Person("Fred", 27)
		makeFriendWith(susan, fred) //Need contravariant here. Friend[Student] supertype of Friend[Person]
	}
}
