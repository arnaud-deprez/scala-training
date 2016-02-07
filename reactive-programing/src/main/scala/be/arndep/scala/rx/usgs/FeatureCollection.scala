package be.arndep.scala.rx.usgs

/**
	* Created by arnaud.deprez on 7/02/16.
	*/
class FeatureCollection {
	val metadata : MetaData       = null
	val features : Array[Feature] = null

	// yes, @headinthebox sometimes uses folds ;-)
	override def toString() = s"{ 'metadata':'${metadata}', 'features':[${features.map(_.toString()).reduceLeft((x,s)=> s"$x,\n $s")}] }";
}
