import rx.lang.scala.Observable

//import for scala duration
import scala.concurrent.duration._
import scala.language.postfixOps._

val ticks: Observable[Long] = Observable.interval(1 second)

val evens: Observable[Long] = ticks.filter(_%2 == 0)

val bufs: Observable[Seq[Long]] = evens.slidingBuffer(count = 2, skip = 1)

val s = bufs.subscribe(println(_))

Thread sleep((10 second).toMillis)

s.unsubscribe()