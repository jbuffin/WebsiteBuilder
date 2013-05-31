package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.templates.Html
import models.widgets.WidgetTypeEnum
import models.widgets.WidgetTypeEnum._

object Widgets extends Controller {
	
	def getWidgetList(pageId: Long): List[Long] = {
		Page.getWidgetsByPageId(pageId)
	}
	
	def getTheWidget(widgetId: Long): Html = {
		val widgetType = "Text";
		val widgetChoose = widgetTypeChooser(WidgetTypeEnum.withName(widgetType))
		Logger.debug(widgetChoose.toString())
		try {
			views.html.sites.widgets.textWidget(models.widgets.Text.getById(widgetId).get)
		}
		catch {
			case nse: NoSuchElementException =>
				views.html.sites.widgets.textWidget(models.widgets.Text("", ""))
		}
	}
	
	def widgetTypeChooser(widgetType: WidgetTypeEnum) = widgetType match {
		case Text => models.widgets.Text
		case Carousel => models.widgets.Carousel
	}
	
}