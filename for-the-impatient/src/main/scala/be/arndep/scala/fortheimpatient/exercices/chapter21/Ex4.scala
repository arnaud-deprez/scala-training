package be.arndep.scala.fortheimpatient.exercices.chapter21

import scala.io.StdIn
import scala.language.implicitConversions

/**
 * Created by Arnaud on 23-05-15.
 */
object Ex4 extends App {
	abstract class TypeToObtain { type t }
	object aString extends TypeToObtain { type t = String }
	object anInt extends TypeToObtain { type t = Int }
	object aDouble extends TypeToObtain { type t = Double }

	class ReadDsl(val what: TypeToObtain) {
		def askingFor(ask: String): this.type = {
			print(ask + ": ")

			val value = what match {
				case `aString` => StdIn.readLine()
				case `anInt` => StdIn.readInt()
				case `aDouble` => StdIn.readDouble()
			}
			println("You wrote " + value)

			this
		}

		def and(what: TypeToObtain) = Obtain(what)
	}
	def Obtain(what: TypeToObtain) = new ReadDsl(what)
	implicit def typeToObtain2ReadDsl(from: TypeToObtain): ReadDsl = new ReadDsl(from)

	println(aString askingFor "Your name" and anInt askingFor "Your age" and aDouble askingFor "Your weight")
}
