package be.arndep.scala.rx

import be.arndep.scala.rx.usgs.Magnitude.Magnitude
import org.scalatest.{FlatSpec, Matchers}
import be.arndep.scala.rx.geocode._
import be.arndep.scala.rx.usgs._
import rx.lang.scala.Observable

import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global

//import for scala duration
import scala.concurrent.duration._
import scala.language.postfixOps

/**
	* Created by arnaud.deprez on 13/02/16.
	*/
class EarthQuakesTest extends FlatSpec with Matchers{
	it should "be possible to map and filtering" in {
		val quakes = Usgs()

		val major = quakes.
			map(q => (q.geometry, Magnitude(q.properties.magnitude))).
			filter({case (loc,mag) => mag >= Magnitude.Major})

		val subscription = major.subscribe(t => println(s"Magnitude ${t._2} quake at ${t._1}"))

		Thread sleep((10 second).toMillis)

		subscription.unsubscribe()
	}

	it should "be possible to reverse Geocode to Country" in {
		val withCountry = Usgs().map(quakes => {
			val country: Future[CountrySubdivision] = ReverseGeocode(quakes.geometry)
			Observable.from(country.map(country => (Magnitude(quakes.properties.magnitude), country)))
		})
		val m: Observable[(Magnitude, CountrySubdivision)] = withCountry.flatten
		val subscription = m.subscribe(t => println(s"Magnitude ${t._1} quake at ${t._2}"))
		Thread sleep((10 second).toMillis)
		subscription.unsubscribe()

		val c: Observable[(Magnitude, CountrySubdivision)] = withCountry.flatten
		val subscription2 = c.subscribe(t => println(s"Magnitude ${t._1} quake at ${t._2}"))
		Thread sleep((10 second).toMillis)
		subscription2.unsubscribe()
	}
}
