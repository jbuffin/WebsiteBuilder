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
			val listOfWidgets: List[Long] = Widgets.getWidgetList(page.pageId)
			if (listOfWidgets.length != 4) {
				Logger.error("Not enough widgets: "+listOfWidgets.length)
				throw new NoSuchElementException
			}
			PageTypeEnum.withName(pageType.typeName) match {
				case ROOT => Ok(views.html.sites.templates.gracechurch.index(page.title, listOfWidgets))
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

	def getAllSitesAsJson = Action {
		Ok(Json.toJson(Site.getAll map { site =>
			Json.obj("id" -> site.siteId.toString, "siteName" -> site.siteName, "hostName" -> site.hostName)
		}))
	}

	implicit val siteRds = (
		(__ \ "id").read[Long] and
		(__ \ "siteName").read[String] and
		(__ \ "hostName").read[String]
	) tupled

	def newSiteFromJson = Action(parse.json) { request =>
		request.body.validate[(Long, String, String)].map {
			case (id, siteName, hostName) => Ok(Site.create(Site(siteName, hostName)).toString)
		}.recoverTotal {
			e => BadRequest("Detected error: "+JsError.toFlatJson(e))
		}
	}
	
	def getAllPagesBySiteAsJson(siteId: Long) = Action {
		Ok(Json.toJson(Page.getAllBySiteId(siteId) map { page =>
			Json.obj("id" -> page.pageId, "uri" -> page.uri, "title" -> page.title, "pageType" -> page.pageType, "parent" -> page.parent, "siteId" -> page.siteId)
		}))
	}

}
