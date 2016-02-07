package be.arndep.scala.rx

import rx.lang.scala.Observable
import rx.lang.scala.subjects.AsyncSubject

import scala.concurrent.Future

/**
	* Created by arnaud.deprez on 7/02/16.
	*/
object ToObservable {
	def apply[T](future: Future[T]): Observable[T] = {
		val subject = AsyncSubject[T]()

		future.onSuccess{
			case s => {
				subject.onNext(s);
				subject.onCompleted()
			}
		}

		future.onFailure{
			case e => {
				subject.onError(e)
			}
		}

		subject
	}
}
