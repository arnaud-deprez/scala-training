package be.arndep.scala.rx

import org.scalatest.{FlatSpec, Matchers}
import rx.lang.scala.subjects.{AsyncSubject, BehaviorSubject, PublishSubject, ReplaySubject}

/**
	* Created by arnaud.deprez on 7/02/16.
	*/
class SubjectsTest extends FlatSpec with Matchers {

	/*
  Banana: 42
  Apple: 42
  Banana: 4711
  Banana.
  Cranberry.
   */
	"PublishSubject" should "be a channel" in {
		val channel = PublishSubject[Int]()
		val a = channel.subscribe(x => println(s"Apple: $x"), e => println(s"Apple~ $e"), () => println(s"Apple."))
		val b = channel.subscribe(x => println(s"Banana: $x"), e => println(s"Banana~ $e"), () => println(s"Banana."))

		channel.onNext(42)

		a.unsubscribe()

		channel.onNext(4711)
		channel.onCompleted()

		val c = channel.subscribe(x => println(s"Cranberry: $x"), e => println(s"Cranberry~ $e"), () => println(s"Cranberry."))

		channel.onNext(13)
	}

	/*
	Banana: 42
	Apple: 42
	Banana: 4711
	Banana.
	Cranberry: 42
	Cranberry: 4711
	Cranberry.
	 */
	"ReplaySubject" should "be a channel" in {
		val channel = ReplaySubject[Int]()
		val a = channel.subscribe(x => println(s"Apple: $x"), e => println(s"Apple~ $e"), () => println(s"Apple."))
		val b = channel.subscribe(x => println(s"Banana: $x"), e => println(s"Banana~ $e"), () => println(s"Banana."))

		channel.onNext(42)

		a.unsubscribe()

		channel.onNext(4711)
		channel.onCompleted()

		val c = channel.subscribe(x => println(s"Cranberry: $x"), e => println(s"Cranberry~ $e"), () => println(s"Cranberry."))

		channel.onNext(13)
	}

	/*
	Apple: 2013
	Banana: 2013
	Banana: 42
	Apple: 42
	Banana: 4711
	Banana.
	Cranberry.
	 */
	"BehaviorSubject" should "be a cache" in {
		val channel = BehaviorSubject(2013)
		val a = channel.subscribe(x => println(s"Apple: $x"), e => println(s"Apple~ $e"), () => println(s"Apple."))
		val b = channel.subscribe(x => println(s"Banana: $x"), e => println(s"Banana~ $e"), () => println(s"Banana."))

		channel.onNext(42)

		a.unsubscribe()

		channel.onNext(4711)
		channel.onCompleted()

		val c = channel.subscribe(x => println(s"Cranberry: $x"), e => println(s"Cranberry~ $e"), () => println(s"Cranberry."))

		channel.onNext(13)
	}

	/*
	Banana: 4711
	Banana.
	Cranberry: 4711
	Cranberry.
	 */
	"AsyncSubject" should "be a Future" in {
		val channel = AsyncSubject[Int]()
		val a = channel.subscribe(x => println(s"Apple: $x"), e => println(s"Apple~ $e"), () => println(s"Apple."))
		val b = channel.subscribe(x => println(s"Banana: $x"), e => println(s"Banana~ $e"), () => println(s"Banana."))

		channel.onNext(42)

		a.unsubscribe()

		channel.onNext(4711)
		channel.onCompleted()

		val c = channel.subscribe(x => println(s"Cranberry: $x"), e => println(s"Cranberry~ $e"), () => println(s"Cranberry."))

		channel.onNext(13)
	}
}
