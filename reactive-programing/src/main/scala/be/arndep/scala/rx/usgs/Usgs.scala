package be.arndep.scala.rx.usgs

import retrofit.client.Response
import retrofit.http.GET
import retrofit.{Callback, RestAdapter, RetrofitError}
import rx.lang.scala.Observable
import rx.lang.scala.subjects.AsyncSubject

/**
	* Created by arnaud.deprez on 7/02/16.
	*/
object Usgs {
	private val restAdapter = new RestAdapter.Builder().setEndpoint("http://earthquake.usgs.gov").build()

	def apply(): Observable[Feature] = {

		val subject = AsyncSubject[FeatureCollection]()

		restAdapter.create(classOf[Usgs]).get(new Callback[FeatureCollection] {

			def failure(error: RetrofitError): Unit = {
				subject.onError(error.getCause)
			}

			def success(t: FeatureCollection, response: Response): Unit = {
				subject.onNext(t)
				subject.onCompleted()
			}

		})

		subject.flatMap(collection => Observable.from(collection.features))
	}
}

private trait Usgs {
//	@GET("/earthquakes/feed/geojson/all/day")
	@GET("/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-01-02")
	def get(callback: Callback[FeatureCollection])
}
