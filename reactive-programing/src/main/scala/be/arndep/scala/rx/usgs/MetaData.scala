package be.arndep.scala.rx.usgs

import java.util.Date

import com.google.gson.annotations.SerializedName

/**
	* Created by arnaud.deprez on 7/02/16.
	*/
class MetaData {
	@SerializedName("generated")
	val _generated: Long     = 0L
	@transient
	lazy val generated: Date = new Date(_generated)
	val url: String          = null
	val title: String        = null
	val api: String          = null
	val count: Int           = 0
	val status: Int          = 0

	override def toString() = s"{ 'generated': '$generated', 'url':'$url', 'title':'$title', 'api': '$api', 'count': '$count', 'status': '$status' }"
}
