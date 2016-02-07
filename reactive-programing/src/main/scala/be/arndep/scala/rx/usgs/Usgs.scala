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

		subject.flatMap(collection => Observable(collection.features : _*))
	}
}

private trait Usgs {
	@GET("/earthquakes/feed/geojson/all/day")
	def get(callback: Callback[FeatureCollection])
}
