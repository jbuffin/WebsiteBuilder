package controllers

import play.api._
import play.api.mvc._

import models._

object Application extends Controller {

	def indexWithNoSiteFound = Action { implicit request =>
		Ok(views.html.index(request.domain))
	}
	
	def javascriptRoutes = Action { implicit request =>
		import routes.javascript._
		Ok(
			Routes.javascriptRouter("jsRoutes") (
				
			)
		).as("text/javascript")
	}

}