package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import controllers._

import models._

object Application extends Controller {

	def indexWithNoSiteFound = Action { implicit request =>
		Logger.debug("[Application.indexWithNoSite]: request.domain: '"+request.domain+"'")
		Ok(views.html.notFound(request.domain))
	}

	def adminHome = Authenticated { implicit request =>
	    val username = request.user
		Logger.debug("[Application.adminHome]")
		if(username != "") {
			Ok(views.html.admin())
		} else {
			Redirect(routes.LoginController.login)
		}
	}

	def javascriptRoutes = Action { implicit request =>
		import routes.javascript._
		Ok(
			Routes.javascriptRouter("jsRoutes")(
				SitesApi.getAllSites,
				SitesApi.newSite,
				SitesApi.getAllPagesBySiteId,
				SitesApi.getPageById,
				SitesApi.newPage,
				SitesApi.getPageByUri,
				SitesApi.updatePage
			)
		).as("text/javascript")
	}

}
