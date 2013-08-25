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

object Sites extends Controller with MongoController with Secured {
	def collection: JSONCollection = db.collection[JSONCollection]("pages")

	def getPageFromUri(uri: String = "", editable: Boolean = false) = IsAuthenticated { username =>
		implicit request => {
			Logger.debug("[Sites.getPageFromUri]: request.domain: '"+request.domain+"', uri: '"+uri+"'")
			val site = Site.getSiteByHostName(request.domain).get
			getPageFromSiteIdAndUri(site.siteId, uri, username != "")
		}
	}

	def getPageFromSiteIdAndUri(siteId: Long, uri: String = "", isAdmin: Boolean) = {
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
								goToPage(Site.getSiteById(siteId).get, pagesList, pageMongo, isAdmin)
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

	def goToPage(site: Site, navigation: List[PageMongoWithId], pageMongo: PageMongoWithId, isAdmin: Boolean) = {
		Ok(views.html.sites.index(site.siteName, navigation, pageMongo, isAdmin))
	}

	def getNavigationBySiteId(siteId: Long): Future[List[PageMongoWithId]] = {
		collection.find(Json.obj("page.siteId" -> siteId)).cursor[PageMongoWithId].toList
	}

}
