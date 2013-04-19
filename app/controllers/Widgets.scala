package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.templates.Html

object Widgets extends Controller {
	
	def getWidgetList(siteId: Long): List[Long] = {
		Page.getWidgetsByPageId(1)
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