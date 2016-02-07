package be.arndep.scala.rx.usgs

import be.arndep.scala.rx.Properties

/**
	* Created by arnaud.deprez on 6/02/16.
	*/
class Feature {

	val properties : Properties = null
	val geometry: Point         = null

	override def toString() = s"{ 'properties':'${properties}', 'geometry':'${geometry}' }";
}
