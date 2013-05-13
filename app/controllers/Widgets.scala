package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.templates.Html

object Widgets extends Controller {
	
	def getWidgetList(pageId: Long): List[Long] = {
		Page.getWidgetsByPageId(pageId)
	}
	
	def getTheWidget(widgetId: Long): Html = {
		val widgetType = "Text";
		try {
			views.html.sites.widgets.textWidget(models.widgets.Text.getById(widgetId).get)
		}
		catch {
			case nse: NoSuchElementException =>
				views.html.sites.widgets.textWidget(models.widgets.Text("", ""))
		}
	}
	
}