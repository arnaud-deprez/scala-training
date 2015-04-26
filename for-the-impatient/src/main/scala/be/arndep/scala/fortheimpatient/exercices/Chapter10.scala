package be.arndep.scala.fortheimpatient.exercices

import java.awt.Point
import java.beans.{PropertyChangeEvent, PropertyChangeListener}

/**
 * Created by Arnaud on 01-05-15.
 */
object Chapter10 extends App{

	// 1
	trait RectangleLike {
		this: java.awt.geom.RectangularShape =>
		def translate(dx: Double, dy: Double): RectangleLike = {
			setFrame(getX + dx, getY + dy, getWidth, getHeight)
			this
		}

		def grow(dx: Double, dy: Double): RectangleLike = {
			setFrame(getX, getY, getWidth + dx, getHeight + dy)
			this
		}
	}

	{
		val egg = new _root_.java.awt.geom.Ellipse2D.Double(5, 10, 20, 30) with RectangleLike //Awesome to add behavior on existing classes
		egg.translate(1, 2)
		egg.grow(3, 4)
		println("%f %f %f %f".format(egg.getX, egg.getY, egg.getWidth, egg.getHeight))
	}

	// 2
	class OrderedPoint(var _x: Int, var _y: Int) extends _root_.java.awt.Point(_x, _y) with Ordered[_root_.java.awt.Point] {
		override def compare(that: Point): Int = {
			if (this.getX != that.getX) (this.getX - that.getX).toInt
			else {
				if (this.getY != that.getY) (this.getY - that.getY).toInt
				else 0
			}
		}

		override def toString: String = "(" + getX + ", " + getY + ")"
	}
	val p1 = new OrderedPoint(1, 1)
	println("p1: " + p1 + "= p1: " + p1 + " = " + (p1 == p1))
	val p2 = new OrderedPoint(1, 2)
	println("p1: " + p1 + "< p2: " + p2 + " = " + (p1 < p2))
	val p3 = new OrderedPoint(2, 2)
	println("p2: " + p2 + "< p3: " + p3 + " = " + (p2 < p3))

	// 3 do it

	// 4
	trait Logger {
		def log(message: String)
	}
	trait CryptoLogger extends Logger {
		var key: Int = 3

		abstract override def log(msg: String) { super.log(encrypt(msg)) }
		// Caesar encryptions
		def encrypt(str: String) = str.map((c: Char) => (c + key).asInstanceOf[Char])
	}
	trait ConsoleLogger extends Logger {
		def log(msg: String) { println(msg) }
	}
	{
		val logger = new ConsoleLogger with CryptoLogger
		logger.log("in my logger")
	}

	// 5
	trait PropertyChangeSupport {
		private var listeners = Map[String, Set[_root_.java.beans.PropertyChangeListener]]() withDefaultValue Set()
		// make the interface a bit more scalaesque while we're at it
		def addPropertyChangeListener(propertyName: String, listener: _root_.java.beans.PropertyChangeListener) {
			listeners += propertyName -> (listeners(propertyName) + listener)
		}
		def firePropertyChange(propertyName: String, oldValue: Any, newValue: Any) {
			val ev = new _root_.java.beans.PropertyChangeEvent(this, propertyName, oldValue, newValue)
			listeners(propertyName).foreach(_.propertyChange(ev))
		}
	}
	{
		val pt = new java.awt.Point(3, 4) with PropertyChangeSupport {
			override def setLocation(x: Double, y: Double) {
				firePropertyChange("location", (getX, getY), (x, y))
				super.setLocation(x, y)
			}
		}

		pt.addPropertyChangeListener("location", new PropertyChangeListener {
			override def propertyChange(ev: PropertyChangeEvent): Unit = {
				println(ev.getSource + "." + ev.getPropertyName + " changed from " + ev.getOldValue + " to " + ev.getNewValue)
			}
		})
		pt.setLocation(4.0, 5.0)
	}

	// 6
	// It's impossible due to multiple inheritance: JContainer extends JComponent and Container and both of them extends Component
	// In scala we could define a trait Component and a trait Container for the composite pattern

	// 7 example from here http://joelabrahamsson.com/learning-scala-part-seven-traits/
	{
		trait Swimming {
			def swim() = println("I'm swimming")
		}
		trait Flying {
			final val flyMessage: String = "I'm a " + quality + " flyer"
			protected def quality: String
			final def fly() = println(flyMessage)
		}
		abstract class Bird

		class Penguin extends Bird with Swimming

		class Pigeon extends Bird with Swimming with Flying {
			protected def quality = "good"
		}

		class Hawk extends Bird with Swimming with Flying {
			protected def quality = "excellent"
		}

		class Frigatebird extends Bird with Flying {
			protected def quality = "excellent"
		}

		val flyingBirds = List(
			new Pigeon,
			new Hawk,
			new Frigatebird)

		flyingBirds.foreach(bird => bird.fly())

		val swimmingBirds = List(
			new Pigeon,
			new Hawk,
			new Penguin)

		swimmingBirds.foreach(bird => bird.swim())
	}

	//8 & 9
	{
		trait Buffering extends java.io.InputStream {
			val buffIn = new java.io.BufferedInputStream(this)

			override def read(): Int = {
				buffIn.read()
			}
		}
		{
			val fis = new java.io.FileInputStream("files/numbers.txt") with Buffering
			var b: Int = 0

			while ( { b = fis.read(); b	} != -1) {
				println("Read: " + b)
			}
		}
	}

	// 9
	{
		trait Buffering extends java.io.InputStream with Logger {
			val buffIn = new java.io.BufferedInputStream(this)

			override def read(): Int = {
				val b = buffIn.read()
				if (b != -1) log("Read byte %02X (%s)".format(b, b.asInstanceOf[Char]))
				else log("Read EOF")
				b
			}
		}
		{
			val fis = new java.io.FileInputStream("files/numbers.txt") with ConsoleLogger with Buffering
			var b: Int = 0

			while ( {
				b = fis.read(); b
			} != -1) {}
		}
	}

	//10
	trait IterableInputStream extends java.io.InputStream with Iterable[Byte] {
		def iterator: Iterator[Byte] = new Iterator[Byte] {
			def hasNext = if (available > 0) true else false
			def next = read().asInstanceOf[Byte]
		}
	}
	{
		val fis = new java.io.FileInputStream("files/numbers.txt") with IterableInputStream
		for (i <- fis) print(i.asInstanceOf[Char])
	}
}
