package models.pages

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class PageType(typeName: String, pageTypeId: Long = -1)

object PageType {
	
	val simple = {
		get[String]("page_types.type_name") ~
		get[Long]("page_types.type_id") map {
			case type_name ~ type_id => PageType(type_name, type_id)
		}
	}
	
	def getAll: List[PageType] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * from page_types
				"""
			).as(PageType.simple *)
		}
	}
	
	def getPageByTypeId(pageId: Long) = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select page_type from pages
						where page_id = {page_id}
				"""
			).on(
					'page_id -> pageId
				).as(get[Long]("page_id").singleOpt map {
						case page_type => PageType.getById(page_type.get)
					})
		}
	}
	
	def getByTypeName(typeName: String): Option[PageType] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * from page_types
						where type_name = {type_name}
				"""
			).on(
				'type_name -> typeName
			).as(PageType.simple.singleOpt)
		}
	}
	
	def getById(pageTypeId: Long): Option[PageType] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * from page_types
						where type_id = {type_id}
				"""
			).on(
				'type_id -> pageTypeId
			).as(PageType.simple.singleOpt)
		}
	}
	
}