package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import play.api.Logger

case class Page(uri: String,
	title: String,
	pageType: Long,
	parent: Long,
	siteId: Long,
	pageId: Long = -1)

object Page {

	val simple = {
		get[String]("pages.uri") ~
			get[String]("pages.title") ~
			get[Long]("pages.page_type") ~
			get[Long]("pages.parent") ~
			get[Long]("pages.page_id") ~
			get[Long]("pages.site_id") map {
				case (uri ~ title ~ page_type ~ parent ~ page_id ~ site_id) => Page(uri, title, page_type, parent, site_id, page_id)
			}
	}

	def getPageByUri(siteId: Long, uri: String): Option[Page] = {
		Logger.debug("[Page.getPageByUri]: siteId: "+siteId+", uri: '"+uri+"'")
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * from pages
						where site_id = {site_id}
							and uri = {uri}
				"""
			).on(
					'site_id -> siteId,
					'uri -> uri
				).as(Page.simple.singleOpt)
		}
	}
	
	def create(page: Page): Long = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
				insert into pages (uri, title, page_type, parent, site_id) values (
					{uri}, {title}, {page_type}, {parent}, {site_id}
				)
				""").on(
					'uri -> page.uri,
					'title -> page.title,
					'page_type -> page.pageType,
					'parent -> page.parent,
					'site_id -> page.siteId).executeInsert()
		} match {
			case Some(long) => long
			case None => -1
		}
	}
	
	def getWidgetsByPageId(pageId: Long): List[Long] = {
		Logger.debug(pageId.toString)
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select widget_id from page_widgets
						where page_id = {page_id}
				"""
			).on(
					'page_id -> pageId
				).as(get[Long]("widget_id")*)
		}
	}

}
