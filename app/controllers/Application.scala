package controllers

import play.api._
import play.api.mvc._

import models._

object Application extends Controller {

	def indexWithNoSiteFound = Action { implicit request =>
		Logger.error("[Application.indexWithNoSite]: request.domain: '"+request.domain+"'")
		Ok(views.html.notFound(request.domain))
	}
	
	def javascriptRoutes = Action { implicit request =>
		import routes.javascript._
		Ok(
			Routes.javascriptRouter("jsRoutes") (
				
			)
		).as("text/javascript")
	}

}