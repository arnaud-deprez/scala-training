package be.arndep.scala.rx

import be.arndep.scala.rx.usgs.Magnitude.Magnitude
import be.arndep.scala.rx.geocode._
import be.arndep.scala.rx.usgs._
import rx.lang.scala.Observable

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

/**
	* Created by arnaud.deprez on 6/02/16.
	*/
object EarthQuakes {

	def quakes(): Observable[Feature] = Usgs()

	def major(): Observable[(Point, Magnitude)] = ofMagnitude(Magnitude.Major)

	def ofMagnitude(atLeast: Magnitude) = {

		quakes().map(quake => (quake.geometry, Magnitude(quake.properties.magnitude))).filter {
			case (location, magnitude) => magnitude >= atLeast
		}

	}

	def withCountry(flatten: Observable[Observable[(Feature, CountrySubdivision)]] => Observable[(Feature, CountrySubdivision)])
	: Observable[(Feature, CountrySubdivision)] = {
		flatten(quakes().map(quake => {
			val country: Future[CountrySubdivision] = ReverseGeocode(quake.geometry)
			ToObservable(country.map(country => (quake,country)))
				.filter{ case(quake, country) => country.countryName != null }
		}))
	}

	def withCountryMerged(): Observable[(Feature, CountrySubdivision)] = {
		withCountry(xss => xss.flatten)
	}

	def withCountryConcatenated(): Observable[(Feature, CountrySubdivision)] = {
		withCountry(xss => xss.concat)
	}

	def groupedByCountry(): Observable[(String, Observable[(Feature, CountrySubdivision)])] = {
		withCountryMerged().groupBy{ case (quake, country) => country.countryName }
	}
}