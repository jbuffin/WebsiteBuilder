package controllers

import play.api._
import play.api.mvc._

import models._

object Sites extends Controller {

	def index(siteId: Long) = Action {
		try {
			Ok(views.html.sites.index(Site.getSiteById(siteId).get))
		}
		catch {
			case nse: NoSuchElementException =>
				Ok(views.html.index("index"))
		}
	}

}