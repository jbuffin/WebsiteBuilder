package controllers

import models._
import play.api._
import play.api.mvc._

object DefaultDataInit {

	def insert() = {
		Logger.info("DefaultDataInit: inserting data")
		if (Site.getAll.isEmpty) {
			Seq(
				Site("gracechurch", "gracechurch.net", 1),
				Site("uswarrior","uswarrior.com", 2)
			).foreach(Site.create)
		}
		Logger.info("DefaultDataInit: done")
	}

}