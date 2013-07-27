package models.pages

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

	def getAllBySiteId(siteId: Long) = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * from pages
						where site_id = {site_id}
				"""
			).on(
					'site_id -> siteId
				).as(Page.simple *)
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

	def getNumRowsByPageId(pageId: Long): Long = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select count(1) from page_rows
						where page_id = {page_id}
				"""
			).on(
					'page_id -> pageId
				).as(
						(get[Long]("count") map {
							case count => count
						}).singleOpt).getOrElse(-1)
		}
	}

	def getRowNumsByPageId(pageId: Long): List[Long] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select page_row_id from page_rows
						where page_id = {page_id}
						order by row_order
				"""
			).on(
					'page_id -> pageId
				).as(get[Long]("page_row_id")*)
		}
	}

	def getWidgetsByRow(rowNum: Int): List[Long] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select widget_id from widget
						where page_row = {page_row}
						order by row_order
				"""
			).on(
					'page_row -> rowNum
				).as(get[Long]("widget_id")*)
		}
	}

	def getWidgetsByPageIdSortedByRow(pageId: Long): List[List[Long]] = {
		val rowNums = getRowNumsByPageId(pageId)
		List.tabulate(rowNums.length)(index => {
			val rowNum = rowNums(index)
			DB.withConnection { implicit connection =>
				SQL(
					"""
						select widget_id from widget
							where page_row = {page_row}
							order by row_order
					"""
				).on(
						'page_row -> (rowNum)
					).as(get[Long]("widget_id")*)
			}
		})
	}

	def getWidgetsByPageId(pageId: Long): List[Long] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select widget_id from widget
						where page_id = {page_id}
						order by page_order
				"""
			).on(
					'page_id -> pageId
				).as(get[Long]("widget_id")*)
		}
	}

}
