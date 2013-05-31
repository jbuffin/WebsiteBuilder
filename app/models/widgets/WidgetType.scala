package models.widgets

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class WidgetType(widgetType: String,
	typeId: Long)

object WidgetType {

	val simple = {
		get[String]("widget_types.type_name") ~
			get[Long]("widget_types.type_id") map {
				case type_name ~ type_id => WidgetType(type_name, type_id)
			}
	}

	def getAll: List[WidgetType] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * from widget_types
				"""
			).as(WidgetType.simple *)
		}
	}

	def getByTypeName(typeName: String): Option[WidgetType] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * from widget_types
						where type_name = {type_name}
				"""
			).on(
					'type_name -> typeName
				).as(WidgetType.simple.singleOpt)
		}
	}

	def getById(typeId: Long): Option[WidgetType] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * from widget_types
						where type_id = {type_id}
				"""
			).on(
					'type_id -> typeId
				).as(WidgetType.simple.singleOpt)
		}
	}

}