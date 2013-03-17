package controllers

import play.api._
import play.api.mvc._

import models._

object Sites extends Controller {
	
	def index(siteId: Long) = Action {
		Ok(views.html.sites.index(Site.getSiteById(siteId).get))
	}
	
}