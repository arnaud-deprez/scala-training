package be.arndep.scala.akka.link

import java.util.concurrent.Executor

import com.ning.http.client.AsyncHttpClient

import scala.concurrent.{Future, Promise}

/**
	* Created by arnaud.deprez on 18/02/16.
	*/
object WebClient {
	val client = new AsyncHttpClient

	def get(url: String)(implicit exec: Executor): Future[String] = {
		val f = client.prepareGet(url).execute()
		val p = Promise[String]()
		f.addListener(new Runnable {
			override def run(): Unit = {
				val response = f.get
				if (response.getStatusCode < 400)
					p.success(response.getResponseBodyExcerpt(131072))
				else p.failure(BadStatus(response.getStatusCode.toString))
			}
		}, exec)
		p.future
	}

	def shutdown(): Unit = client.closeAsynchronously()
}
