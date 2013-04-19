package controllers

import models._
import models.widgets._
import play.api.Logger

object DefaultDataInit {

	def insert() = {
		if (Site.getAll.isEmpty) {
			Logger.debug("DefaultDataInit: inserting data")
			Seq(
				Site("gracechurch", "gracechurch.net"),
				Site("uswarrior www", "www.uswarrior.com")
			).foreach(Site.create)

//			Page.create(Page("", "Default Page", PageType.getByTypeName("ROOT").get.pageTypeId, -1, Site.getSiteByHostName("gracechurch.net").get.siteId))
			Logger.debug("DefaultDataInit: done")
		}
	}

}