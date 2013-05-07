package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.templates.Html

object Sites extends Controller {

	def index = Action { implicit request =>
		Logger.debug("[Sites.index]: request.domain: '"+request.domain+"'")
		try {
			Ok(views.html.sites.index(Widgets.getWidgetList(Site.getSiteByHostName(request.domain).get.siteId)))
		}
		catch {
			case nse: NoSuchElementException =>
				Logger.error(nse.getMessage())
				Redirect(routes.Application.indexWithNoSiteFound)
		}
	}
	
	def getPageFromUri(page: String) = Action { implicit request =>
		//Ok(views.html.sites.index(Widgets.getWidgetList(Site.getSiteByHostName(request.domain).get.siteId)))
		Redirect(routes.Sites.index)
	}

}
