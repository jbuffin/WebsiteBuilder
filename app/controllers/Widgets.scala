package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.templates.Html
import models.widgets.WidgetTypeEnum
import models.widgets.WidgetTypeEnum._

object Widgets extends Controller {
	
	def getWidgetList(pageId: Long): List[Long] = {
		val widgets = Page.getWidgetsByPageId(pageId)
		Logger.debug(widgets.toString)
		widgets
	}
	
	def getTheWidget(widgetId: Long): Html = {
		val widgetType = models.widgets.WidgetType.getWidgetTypeById(widgetId).get
		try {
			widgetTypeChooser(WidgetTypeEnum.withName(widgetType.widgetType), widgetId)
		}
		catch {
			case nse: NoSuchElementException =>
				views.html.sites.widgets.textWidget(models.widgets.Text("", ""))
		}
	}
	
	def widgetTypeChooser(widgetType: WidgetTypeEnum, widgetId: Long) = widgetType match {
		case Text => views.html.sites.widgets.textWidget(models.widgets.Text.getByWidgetId(widgetId).get)
		case Carousel => views.html.sites.widgets.carouselWidget(List())
	}
	
}