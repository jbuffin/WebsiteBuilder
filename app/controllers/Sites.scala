package controllers

import models.Site
import models.pages._
import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json.Json.toJsFieldJsValueWrapper
import play.api.libs.json._
import play.api.mvc.Action
import play.api.mvc.Controller
import reactivemongo.api._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.Cursor
import scala.concurrent.Future
import models.pages.JsonFormats._
import play.api.data.Form

object Sites extends Controller with MongoController {
	def collection: JSONCollection = db.collection[JSONCollection]("pages")

	def getPageFromUri(uri: String = "", editable: Boolean = false) = Action { implicit request =>
		Logger.debug("[Sites.getPageFromUri]: request.domain: '"+request.domain+"', uri: '"+uri+"'")
		val site = Site.getSiteByHostName(request.domain).get
		getPageFromSiteIdAndUri(site.siteId, uri)
	}

	def getPageFromSiteIdAndUri(siteId: Long, uri: String = "") = {
		Logger.debug("[Sites.getPageFromSiteIdAndUri]: siteId: '"+siteId+"', uri: '"+uri+"'")
		try {
			Async {
				val query = Json.obj("page.uri" -> uri, "page.siteId" -> siteId)
				val futurePage = collection.find(query).one[PageMongoWithId]
				futurePage.map {
					case Some(pageMongo) => {
						Async {
							val pageNav = getNavigationBySiteId(siteId)
							pageNav.map { pagesList =>
								goToPage(Site.getSiteById(siteId).get, pagesList, pageMongo)
							}
						}
					}
					case None => Redirect(routes.Application.indexWithNoSiteFound)
				}
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

	def goToPage(site: Site, navigation: List[PageMongoWithId], pageMongo: PageMongoWithId) = {
		Ok(views.html.sites.index(site.siteName, navigation, pageMongo))
	}

	def getNavigationBySiteId(siteId: Long): Future[List[PageMongoWithId]] = {
		collection.find(Json.obj("page.siteId" -> siteId)).cursor[PageMongoWithId].toList
	}
	/*
	def getAllPageTypesAsJson = Action {
		Ok(Json.toJson(PageType.getAll map { pageType =>
			Json.obj("typeName" -> pageType.typeName, "pageTypeId" -> pageType.pageTypeId)
		}))
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
*/
}
