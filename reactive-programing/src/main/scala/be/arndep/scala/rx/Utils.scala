package be.arndep.scala.rx

import _root_.rx.lang.scala.{Notification, Observable}

/**
	* Created by arnaud.deprez on 7/02/16.
	*/
object Utils {
	/**
		* Print an observable to stdout, blocking the calling thread.
		*/
	def displayObservable[T](o: Observable[T]): Unit = {
		println()
		toPrintableNotifications(o).toBlocking.foreach(println(_))
		println()
	}

	def toPrintableNotifications[T](o: Observable[T]): Observable[String] = {
		val t0 = System.currentTimeMillis
		for ((millis, notif) <- o.materialize.timestamp)
			yield f"t = ${(millis-t0)/1000.0}%.3f: ${notificationToString(notif)}"
	}

	/**
		* does what Notification.toString (or its subclasses) should do
		*/
	def notificationToString[T](n: Notification[T]): String = n match {
		case Notification.OnNext(value) => s"OnNext($value)"
		case Notification.OnError(err) => s"OnError($err)"
		case Notification.OnCompleted => "OnCompleted()"
	}
}
