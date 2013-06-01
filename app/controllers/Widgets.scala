package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.templates.Html
import models.widgets.WidgetTypeEnum
import models.widgets.WidgetTypeEnum._
import models.widgets.Carousel
import models.pages.Page

object Widgets extends Controller {
	
	def getWidgetList(pageId: Long): List[Long] = {
		Page.getWidgetsByPageId(pageId)
	}
	
	def getTheWidget(widgetId: Long): Html = {
		try {
			val widgetType = models.widgets.WidgetType.getWidgetTypeById(widgetId).get
			widgetTypeChooser(WidgetTypeEnum.withName(widgetType.widgetType), widgetId)
		}
		catch {
			case nse: NoSuchElementException =>
				views.html.sites.widgets.textWidget(models.widgets.Text.emptyTextWidget)
		}
	}
	
	def widgetTypeChooser(widgetType: WidgetTypeEnum, widgetId: Long) = widgetType match {
		case Text => views.html.sites.widgets.textWidget(models.widgets.Text.getByWidgetId(widgetId).get)
		case WidgetTypeEnum.Carousel => views.html.sites.widgets.carouselWidget(models.widgets.Carousel.getByWidgetId(widgetId).images)
		case _ => views.html.sites.widgets.textWidget(models.widgets.Text.emptyTextWidget)
	}
	
}