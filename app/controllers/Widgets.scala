package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.templates.Html
import models.widgets.WidgetTypeEnum
import models.widgets.WidgetTypeEnum._
import models.widgets.Carousel
import models.pages.Page
import play.api.libs.json.Json
import models.widgets.WidgetType

object Widgets extends Controller {
	
	def getWidgetList(pageId: Long): List[Long] = {
		val numRows = Page.getNumRowsByPageId(pageId)
		Page.getWidgetsByPageId(pageId)
	}
	
	def getWidgetsByPageIdAsJson(pageId: Long) = Action {
		Ok(Json.toJson(getWidgetList(pageId) map { widgetId =>
			WidgetTypeEnum.withName(WidgetType.getWidgetTypeById(widgetId).get.widgetType) match {
				case Text => models.widgets.Text.getByWidgetId(widgetId) map { widget =>
					Json.obj("id" -> widget.id.toString, "widgetType" -> "Text", "title" -> widget.title, "text" -> widget.text)				
				}
				case WidgetTypeEnum.Carousel => models.widgets.Carousel.getByWidgetId(widgetId) map { carouselWidget =>
					Json.obj("id" -> carouselWidget.widgetId, "widgetType" -> "Carousel")
				}
			}
		}))
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
		case WidgetTypeEnum.Carousel => views.html.sites.widgets.carouselWidget(models.widgets.Carousel.getByWidgetId(widgetId).get.images)
		case _ => views.html.sites.widgets.textWidget(models.widgets.Text.emptyTextWidget)
	}
	
}