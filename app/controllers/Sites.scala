package controllers

import play.api._
import play.api.mvc._
import models._
import models.widgets._
import play.api.templates.Html
import models.pages.Page
import models.pages.PageType
import models.pages.PageTypeEnum
import models.pages.PageTypeEnum._
import play.api.libs.json
import play.api.libs.json._
import play.api.libs.functional.syntax._

object Sites extends Controller {

	def getPageFromUri(uri: String = "") = Action { implicit request =>
		Logger.debug("[Sites.getPageFromUri]: request.domain: '"+request.domain+"', uri: '"+uri+"'")
		try {
			val site = Site.getSiteByHostName(request.domain).get
			val page = Page.getPageByUri(site.siteId, uri).get
			pageTypeChooser(PageType.getById(page.pageType).get, page)
		}
		catch {
			case nse: NoSuchElementException => {
				Logger.error(nse.getMessage())
				nse.printStackTrace()
				Redirect(routes.Application.indexWithNoSiteFound)
			}
		}
	}

	def getPageFromSiteIdAndUri(siteId: Long, uri: String = "") = Action { implicit request =>
		Logger.debug("[Sites.getPageFromSiteIdAndUri]: siteId: '"+siteId+"', uri: '"+uri+"'")
		try {
			val page = Page.getPageByUri(siteId, uri).get
			pageTypeChooser(PageType.getById(page.pageType).get, page)
		}
		catch {
			case nse: NoSuchElementException => {
				Logger.error(nse.getMessage())
				nse.printStackTrace()
				Redirect(routes.Application.indexWithNoSiteFound)
			}
		}
	}

	def pageTypeChooser(pageType: PageType, page: Page) = {
		try {
			PageTypeEnum.withName(pageType.typeName) match {
				case ROOT => Ok(views.html.sites.templates.gracechurch.index(page.title, page.siteId, Widgets.getWidgetList(page.pageId)))
				case _ => NotFound
			}
		}
		catch {
			case nse: NoSuchElementException => {
				Logger.error(nse.getMessage())
				nse.printStackTrace()
				Redirect(routes.Application.indexWithNoSiteFound)
			}
		}
	}
	
	def getNavigationBySiteId(siteId: Long) = {
		Page.getAllBySiteId(siteId)
	}
	
	def getAllPageTypesAsJson = Action {
		Ok(Json.toJson(PageType.getAll map { pageType =>
			Json.obj("typeName" -> pageType.typeName, "pageTypeId" -> pageType.pageTypeId)
		}))
	}

	def getAllSitesAsJson = Action {
		Ok(Json.toJson(Site.getAll map { site =>
			Json.obj("id" -> site.siteId.toString, "siteName" -> site.siteName, "hostName" -> site.hostName)
		}))
	}

	def newSiteFromJson = Action(parse.json) { request =>
		request.body.validate[Site].map { site =>
			Ok(Site.create(Site(site.siteName, site.hostName)).toString)
		}.recoverTotal {
			e => BadRequest("Detected error: "+JsError.toFlatJson(e))
		}
	}
	
	def getAllPagesBySiteAsJson(siteId: Long) = Action {
		Ok(Json.toJson(Page.getAllBySiteId(siteId) map { page =>
			Json.obj("id" -> page.pageId, "uri" -> page.uri, "title" -> page.title, "pageType" -> page.pageType, "parent" -> page.parent, "siteId" -> page.siteId)
		}))
	}
	
	implicit val pageRds = (
		(__ \ "id").read[Long] and
		(__ \ "uri").read[String] and
		(__ \ "title").read[String] and
		(__ \ "pageType").read[Long] and
		(__ \ "parent").read[Long] and
		(__ \ "siteId").read[Long]
	) tupled
	
	def newPageFromJson = Action(parse.json) { request =>
		request.body.validate[(Long, String, String, Long, Long, Long)].map {
			case (id, uri, title, pageType, parent, siteId) => Ok(Page.create(Page(uri, title, pageType, parent, siteId)).toString)
		}.recoverTotal{
			e => BadRequest("Detected error: "+JsError.toFlatJson(e)+"\n"+request.body)
		}
	}

}
