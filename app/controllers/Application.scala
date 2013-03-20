package controllers

import play.api._
import play.api.mvc._

import models._

object Application extends Controller {

	def index = Action { implicit request =>
		try {
			Ok(views.html.sites.index(Site.getSiteByHostName(request.domain).get))
		}
		catch {
			case nse: NoSuchElementException =>
				Ok(views.html.index("index"))
		}
	}
	
	def indexWithNoSiteFound = Action {
		Ok(views.html.index("No Site Found"))
	}
	
	def javascriptRoutes = Action { implicit request =>
		import routes.javascript._
		Ok(
			Routes.javascriptRouter("jsRoutes") (
				
			)
		).as("text/javascript")
	}

}