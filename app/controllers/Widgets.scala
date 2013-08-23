package controllers

import play.api._
import play.api.mvc._
import models._
import play.api.templates.Html
import models.widgets.WidgetTypeEnum
import models.widgets.WidgetTypeEnum._
import models.widgets.Carousel
import play.api.libs.json.Json
import models.widgets.WidgetType
import play.api.libs.json.JsError
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Widgets extends Controller {

/*	def getWidgetList(pageId: Long): List[Long] = {
		val numRows = Page.getNumRowsByPageId(pageId)
		Page.getWidgetsByPageId(pageId)
	}*/

/*	def getWidgetsByPageIdAsJson(pageId: Long) = Action {
		Ok(Json.toJson(getWidgetList(pageId) map { widgetId =>
			WidgetTypeEnum.withName(WidgetType.getWidgetTypeById(widgetId).get.widgetType) match {
				case Text => models.widgets.Text.getByWidgetId(widgetId) map { widget =>
					Json.obj("id" -> widget.id.toString, "widgetType" -> "Text", "text" -> widget.text)
				}
				case WidgetTypeEnum.Carousel => models.widgets.Carousel.getByWidgetId(widgetId) map { carouselWidget =>
					Json.obj("id" -> carouselWidget.widgetId, "widgetType" -> "Carousel")
				}
			}
		}))
	}*/

/*	def getTheWidget(widgetId: Long): Html = {
		try {
			val widgetType = models.widgets.WidgetType.getWidgetTypeById(widgetId).get
			widgetTypeChooser(WidgetTypeEnum.withName(widgetType.widgetType), widgetId)
		}
		catch {
			case nse: NoSuchElementException =>
				views.html.sites.widgets.textWidget(models.widgets.Text.emptyTextWidget)
		}
	}*/

/*	def widgetTypeChooser(widgetType: WidgetTypeEnum, widgetId: Long) = widgetType match {
		case Text => views.html.sites.widgets.textWidget(models.widgets.Text.getByWidgetId(widgetId).getOrElse(models.widgets.Text.emptyTextWidget))
		case WidgetTypeEnum.Carousel => views.html.sites.widgets.carouselWidget(models.widgets.Carousel.getByWidgetId(widgetId).get.images)
		case _ => views.html.sites.widgets.textWidget(models.widgets.Text.emptyTextWidget)
	}*/

	def getTextWidgetHtmlByIdAsJSON(textWidgetId: Long) = Action {
		Ok(Json.toJson(models.widgets.Text.getByTextWidgetId(textWidgetId) map { textWidget =>
			Json.obj("textWidgetId" -> textWidget.id, "text" -> textWidget.text)
		}))
	}

	def updateTextWidgetById = Action(parse.json) { request =>
		request.body.validate[List[models.widgets.Text]].map { textWidgets =>
			textWidgets.map { textWidget =>
				models.widgets.Text.updateById(textWidget)
			}
			Ok(Json.toJson("updateTextWidgetById"))
		}.recoverTotal(
			e => BadRequest("Detected error: "+JsError.toFlatJson(e)+"\n"+request.body)
		)
	}

	implicit val newTextWidget = (
		(__ \ "rowNum").read[Long] ~
		(__ \ "text").read[String]
	) tupled

/*	def createTextWidgetsByPageIdFromJSON(pageId: Long) = Action(parse.json) { request =>
		request.body.validate[List[(Long, String)]].map { textWidgets =>
			val idList = List.tabulate(textWidgets.length)(textWidgets.map { textWidget =>
				val widgetId = models.widgets.Widget.create(
					models.widgets.Widget(
						-1,
						models.widgets.WidgetType.getByTypeName(models.widgets.WidgetTypeEnum.Text.toString()).get.typeId,
						pageId,
						Page.getRowNumsByPageId(pageId)(textWidget._1.toInt-1),
						models.pages.Page.getWidgetsByRow(Page.getRowNumsByPageId(pageId)(textWidget._1.toInt-1).toInt).length+1
					)
				)
				models.widgets.Text.create(models.widgets.Text(textWidget._2, widgetId))
			})
			Ok(Json.toJson(idList))
		}.recoverTotal(
			e => BadRequest("Detected error: "+JsError.toFlatJson(e)+"\n"+request.body)
		)
	}*/

}