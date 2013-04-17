package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.templates.Html

object Sites extends Controller {

	def index = Action { implicit request =>
		Logger.info(request.domain)
		try {
			Ok(views.html.sites.index(Site.getSiteByHostName(request.domain).get.siteId))
		}
		catch {
			case nse: NoSuchElementException =>
				Redirect(routes.Application.indexWithNoSiteFound)
		}
	}
	
	def getPageFromUri(page: String) = Action { implicit request =>
		Ok(views.html.sites.index(Site.getSiteByHostName(request.domain).get.siteId))
	}
	
	def getTheWidget(widgetId: Long): Html = {
		try {
			views.html.sites.widgets.textWidget(models.widgets.Text.getById(widgetId).get)
		}
		catch {
			case nse: NoSuchElementException =>
				views.html.sites.widgets.textWidget(models.widgets.Text("An error occurred", "There was an error retreiving the widget"))
		}
	}

}
