package controllers

import models._
import play.api._
import play.api.mvc._

object DefaultDataInit {

	def insert() = {
		Logger.info("DefaultDataInit: inserting data")
		if (Site.getAll.isEmpty) {
			Seq(
				Site("stumpsoft", "stumpsoft.net")
			).foreach(Site.create)
		}
		Logger.info("DefaultDataInit: done")
	}

}