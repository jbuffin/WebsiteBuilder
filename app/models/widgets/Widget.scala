package models.widgets

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class Widget(id: Long,
	typeId: Long,
	pageId: Long,
	pageRow: Long,
	rowOrder: Long)

object Widget {

	def create(widget: Widget) = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
				insert into widget
					(widget_type, page_id, page_row, row_order)
					values ({widget_type}, {page_id}, {page_row}, {row_order})
				""").on(
					'widget_type -> widget.typeId,
					'page_id -> widget.pageId,
					'page_row -> widget.pageRow,
					'row_order -> widget.rowOrder).executeInsert()
		} match {
			case Some(widgetId) => widgetId
			case None => -1
		}
	}

}