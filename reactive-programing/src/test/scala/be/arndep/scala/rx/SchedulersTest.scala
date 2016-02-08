package be.arndep.scala.rx

import org.junit.Test
import org.scalatest.junit.JUnitSuite
import rx.lang.scala._
import rx.lang.scala.subscriptions.MultipleAssignmentSubscription

/**
	* Since several of the sample function below run on their own thread,
	* or run forever, add @Test by hand in front of the functions you want study.
	* It is best to only have one test enabled.
	*/
class SchedulersTest extends JUnitSuite {
	def fromIterableBad[T](seq: Iterable[T]) : Observable[T] = {
		Observable(observer => {
			try {
				seq.foreach(observer.onNext(_))
			} catch  {
				case e: Throwable => observer.onError(e)
			}

			Subscription()
		})
	}

	def from(start: Integer): Iterable[Integer] = {
		new Iterable[Integer]() {
			def iterator: Iterator[Integer] = {
				var n = start-1;
				new Iterator[Integer] () {
					def hasNext: Boolean =  { n += 1; true }
					def next(): Integer = n
				}
			}
		}
	}

	def attemptI(): Unit = {
		val nats: Observable[Integer] = fromIterableBad(from(0))
		val subscription = nats.subscribe(println(_))
		subscription.unsubscribe()
	}

	def helloScheduler(): Unit = {
		val scheduler: Scheduler = schedulers.NewThreadScheduler()
		println(s"running on ${Thread.currentThread().getName}")
		val subscription = scheduler.createWorker.schedule {
			println(s"running on ${Thread.currentThread().getName}")
		}
		subscription.unsubscribe()
	}

	/**
		* Don't count on this to work!
		* The only guarantee you get is that you can cancel the work
		* between it gets scheduled and before it gets run.
		* (RxJava goes out of the way to make it stop)
		*/
	def attemptII(): Unit = {
		val scheduler: Scheduler = schedulers.NewThreadScheduler()
		val nats: Observable[Integer] = Observable(observer => {
			scheduler.createWorker.schedule{ from(0).foreach(observer.onNext(_)) }
		})

		val subscription = nats.subscribe(println(_))
		subscription.unsubscribe()
		println("You won the lottery")
	}

	// warning: does not catch exceptions and send to onError
	def attemptIII(): Unit = {
		val scheduler: Scheduler = schedulers.NewThreadScheduler()
		val nats: Observable[Integer] = Observable(observer => {
			val iterator = from(0).iterator
			scheduler.createWorker.scheduleRec {
				if(iterator.hasNext) {
					observer.onNext(iterator.next());
				} else {
					observer.onCompleted()
				}
			}
		})
		val subscription = nats.subscribe(println(_))
		subscription.unsubscribe()
		println("we made it work!")
	}

	/**
		* In the slides this is defined as factory method on Observable in the quiz.
		* Perhaps the easiest way to use schedulers in scenarios like the above,
		* since you can directly subscribe to the scheduler.
		*/
	def SchedulerToObservable()(implicit scheduler: Scheduler): Observable[Unit] = {
		Observable(observer => {
			scheduler.createWorker.scheduleRec {
				observer.onNext()
			}
		})
	}

	@Test def unitObservable() {
		implicit val scheduler: Scheduler = schedulers.NewThreadScheduler()
		val observable = SchedulerToObservable()
		observable.subscribe(u => println("unit"))
		println("unitObservable out")
	}

	/*def scheduleRec(outer: Scheduler, work: (=>Unit)=>Unit): Subscription = {
		val subscription = MultipleAssignmentSubscription()
		outer.createWorker.schedule {
			def loop(): Unit = {
				subscription.subscription = s.schedule {
					work { loop() } }
			}
			loop()
		}
		subscription
	}*/

	/*@Test def comeOnBabyOneMoreTime(): Unit = {

		val ns = (Observable(observer =>  {
			var n = 0
			scheduleRec(schedulers.NewThreadScheduler(), self => {
				observer.onNext(n); n += 1
				self
			})}): Observable[Integer]).take(5)

		ns.subscribe(println(_))

		Thread.sleep(1000)

	}*/

	def range(start: Int, count: Int)(implicit s: Scheduler): Observable[Int] = {
		Observable(observer => {
			var i = 0
			SchedulerToObservable().subscribe(u => {
				if (i < count) { observer.onNext(start + i); i += 1 }
				else { observer.onCompleted() }
			})
		})
	}

	@Test def range() {
		implicit val scheduler: Scheduler = schedulers.NewThreadScheduler()
		val xs = range(1, 10)
		xs.subscribe(x => println(x))
		println("range out")
	}
}
