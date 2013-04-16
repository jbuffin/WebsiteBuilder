package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.templates.Html

object Sites extends Controller {

	def index = Action { implicit request =>
		try {
			Ok(views.html.sites.index(List()))
		}
		catch {
			case nse: NoSuchElementException =>
				Redirect(routes.Application.indexWithNoSiteFound)
		}
	}
	
	def getPageFromUri(page: String) = Action {
		Ok(views.html.sites.index(Page.getWidgetsByPageId(Page.getPageByUri("").get.pageId)))
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
