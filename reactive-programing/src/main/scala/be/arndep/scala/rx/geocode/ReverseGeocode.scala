package be.arndep.scala.rx.geocode

import be.arndep.scala.rx.usgs.Point
import retrofit.client.Response
import retrofit.http.{GET, Query}
import retrofit.{Callback, RestAdapter, RetrofitError}

import scala.concurrent.{Future, Promise}

/**
	* Created by arnaud.deprez on 7/02/16.
	*/
object ReverseGeocode {

	private val restAdapter = new RestAdapter.Builder().setEndpoint("http://ws.geonames.org").build()

	def apply(point: Point): Future[CountrySubdivision] = {
		ReverseGeocode(point.latitude, point.longitude)
	}

	def apply(latitude: Double, longitude: Double): Future[CountrySubdivision] = {

		// Promise/Future is isomorphic to Observer/Observable as a Subject

		val promise = Promise[CountrySubdivision]()

		restAdapter.create(classOf[ReverseGeocode]).get(latitude, longitude, new Callback[CountrySubdivision] {

			def failure(error: RetrofitError): Unit = {
				promise.failure(error.getCause)
			}

			def success(t: CountrySubdivision, response: Response): Unit = {
				promise.success(t)
			}

		})

		promise.future
	}
}

private trait ReverseGeocode {
	@GET("/countrySubdivisionJSON")
	def get(@Query("lat")latitude: Double, @Query("lng")longitude: Double, callback: Callback[CountrySubdivision])
}
