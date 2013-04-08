package controllers

import play.api._
import play.api.mvc._

import models._

object Sites extends Controller {

	def index = Action { implicit request =>
		try {
			Ok(views.html.sites.index(Page.getWidgetsByPageId(Page.getPageByUri("").get.pageId)))
		}
		catch {
			case nse: NoSuchElementException =>
				Redirect(routes.Application.indexWithNoSiteFound)
		}
	}
	
	def getPageFromUri(page: String) = Action {
		Ok(views.html.sites.index(Page.getWidgetsByPageId(Page.getPageByUri("").get.pageId)))
	}
	
	def getTheWidget(widgetId: Long) {
		
	}

}
