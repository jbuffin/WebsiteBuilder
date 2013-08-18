package controllers

import models.Site
import models.pages._
import models.pages.PageTypeEnum._
import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.libs.json._
import play.api.mvc.Action
import play.api.mvc.Controller

object Sites extends Controller {

	def getPageFromUri(uri: String = "", editable: Boolean = false) = Action { implicit request =>
		Logger.debug("[Sites.getPageFromUri]: request.domain: '"+request.domain+"', uri: '"+uri+"'")
		try {
			val site = Site.getSiteByHostName(request.domain).get
			val page = Page.getPageByUri(site.siteId, uri).get
			goToPage(page, site, uri, getNavigationBySiteId(site.siteId), Page.getWidgetsByPageIdSortedByRow(page.pageId))
//			pageTypeChooser(PageType.getById(page.pageType).get, page, site)
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
			goToPage(page, Site.getSiteById(siteId).get, uri, getNavigationBySiteId(siteId), Page.getWidgetsByPageIdSortedByRow(page.pageId))
//			pageTypeChooser(PageType.getById(page.pageType).get, page, Site.getSiteById(siteId).get)
		}
		catch {
			case nse: NoSuchElementException => {
				Logger.error(nse.getMessage())
				nse.printStackTrace()
				Redirect(routes.Application.indexWithNoSiteFound)
			}
		}
	}
	
	def goToPage(page: Page, site: Site, uri: String, navigation: List[Page], widgets: List[List[play.api.templates.Html]]) = {
		Ok(views.html.sites.index(page, site.siteName, navigation, widgets))
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
			Ok(Site.create(site).toString)
		}.recoverTotal {
			e => BadRequest("Detected error: "+JsError.toFlatJson(e))
		}
	}

	def getAllPagesBySiteAsJson(siteId: Long) = Action {
		Ok(Json.toJson(Page.getAllBySiteId(siteId) map { page =>
			Json.obj("id" -> page.pageId, "uri" -> page.uri, "title" -> page.title, "pageType" -> page.pageType, "parent" -> page.parent, "siteId" -> page.siteId)
		}))
	}

	def newPageFromJson = Action(parse.json) { request =>
		request.body.validate[Page].map { page =>
			Ok(Page.create(page).toString)
		}.recoverTotal {
			e => BadRequest("Detected error: "+JsError.toFlatJson(e)+"\n"+request.body)
		}
	}

	def addRowsToPage(pageId: Long, numRows: Long) = Action {
		Ok(Json.toJson(Json.obj("rows" -> Page.addRowsByPageId(pageId, numRows))))
	}

}
