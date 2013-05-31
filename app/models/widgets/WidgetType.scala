package models.widgets

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._
import play.api.Logger

case class WidgetType(widgetType: String,
	typeId: Long)

object WidgetType {

	val simple = {
		get[String]("widget_types.type_name") ~
			get[Long]("widget_types.widget_type_id") map {
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
	
	def getWidgetTypeById(widgetId: Long): Option[WidgetType] = {
		Logger.debug(widgetId.toString)
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select widget_type from widget
						where widget_id = {widget_id}
				"""
			).on(
					'widget_id -> widgetId
				).as(get[Long]("widget_type").singleOpt map {
					case widget_type => WidgetType.getById(widget_type.get)
				})
			}
		
	}

	def getById(typeId: Long): Option[WidgetType] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * from widget_types
						where widget_type_id = {type_id}
				"""
			).on(
					'type_id -> typeId
				).as(WidgetType.simple.singleOpt)
		}
	}

}