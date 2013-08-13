package models.widgets

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Text(
	text: String,
	id: Long = -1)

object Text {

	val simple = {
		get[String]("text_widget.text") ~
			get[Long]("text_widget.text_widget_id") map {
				case text ~ text_widget_id => Text(text, text_widget_id)
			}
	}
	
	implicit val textRds = (
		(__ \ "savedHtml").read[String] ~
		(__ \ "textWidgetId").read[Long]
	)(Text.apply _)

	def getByTextWidgetId(textWidgetId: Long): Option[Text] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * from text_widget
						where text_widget_id = {text_widget_id}
				"""
			).on(
					'text_widget_id -> textWidgetId
				).as(Text.simple.singleOpt)
		}
	}

	def getByWidgetId(widgetId: Long): Option[Text] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * from text_widget
						where widget_id = {widget_id}
				"""
			).on(
					'widget_id -> widgetId
				).as(Text.simple.singleOpt)
		}
	}

	def create(textWidget: Text): Long = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
				insert into text_widget (text) values (
					{text}
				)
				""").on(
					'text -> textWidget.text).executeInsert()
		} match {
			case Some(textWidgetId) => textWidgetId
			case None => -1
		}
	}

	def updateById(textWidget: Text) = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
				update text_widget
					set text = {text}
					where text_widget_id = {text_widget_id}
				""").on(
					'text -> textWidget.text,
					'text_widget_id -> textWidget.id
				).executeUpdate()
		}
	}

	def emptyTextWidget = {
		Text("")
	}

}