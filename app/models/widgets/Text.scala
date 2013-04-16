package models.widgets

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class Text(
		title: String,
		text: String,
		id: Long = -1)

object Text {
	
	val simple = {
		get[String]("text_widget.title") ~
		get[String]("text_widget.text") ~
		get[Long]("text_widget.text_widget_id") map {
			case title ~ text ~ text_widget_id => Text(title, text, text_widget_id)
		}
	}
	
	def getById(widgetId: Long): Option[Text] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * from text_widget
						where text_widget_id = {text_widget_id}
				"""
			).on(
				'text_widget_id -> widgetId
			).as(Text.simple.singleOpt)
		}
	}
	
}