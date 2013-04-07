package controllers

import play.api._
import play.api.mvc._

import models._

object Sites extends Controller {

	def index = Action { implicit request =>
		try {
			Ok(views.html.sites.index()
		}
		catch {
			case nse: NoSuchElementException =>
				Redirect(routes.Application.indexWithNoSiteFound)
		}
	}
	
	def getPageFromUri(page: String) = Action {
		Ok(views.html.sites.index(Site.getSiteById(1).get))
	}
	
	def getTheWidget()

}