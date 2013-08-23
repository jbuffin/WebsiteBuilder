package controllers

import play.api._
import play.api.mvc._

import models._

object Application extends Controller {

	def indexWithNoSiteFound = Action { implicit request =>
		Logger.debug("[Application.indexWithNoSite]: request.domain: '"+request.domain+"'")
		Ok(views.html.notFound(request.domain))
	}
	
	def adminHome = Action { implicit request =>
		Logger.debug("[Application.adminHome]")
		Ok(views.html.admin(Site.getSiteByHostName(request.domain).get.siteName))
	}
	
	def javascriptRoutes = Action { implicit request =>
		import routes.javascript._
		Ok(
			Routes.javascriptRouter("jsRoutes") (
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