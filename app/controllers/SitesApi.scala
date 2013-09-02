package controllers

import play.api.Logger
import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.concurrent.Future
import reactivemongo.api._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.json.BSONFormats._
import play.api.data.Form
import models._
import models.pages.JsonFormats._
import models.pages.PageMongo
import models.pages.PageMongoWithId

object SitesApi extends Controller with MongoController with Secured {

	def collection: JSONCollection = db.collection[JSONCollection]("pages")

	def newSite = Action(parse.json) { request =>
		request.body.validate[Site].map { site =>
			Ok(Site.create(site).toString)
		}.recoverTotal {
			e => BadRequest("Detected error: "+JsError.toFlatJson(e))
		}
	}

	def getAllSites = IsAuthenticated { username => implicit request =>
		Logger.debug("[SitesApi.getAllSites]: username: "+username)
		if(username != "") {
			Ok(Json.toJson(Site.getAll map { site =>
				Json.obj("id" -> site.siteId.toString, "siteName" -> site.siteName, "hostName" -> site.hostName)
			}))
		} else {
			notAuthorized
		}
	}

	def newPage(siteId: Long) = Action(parse.json) { request =>
		request.body.validate[PageMongo].map { page =>
			val objectId = BSONObjectID.generate
			val pageWithId = Json.obj("_id" -> objectId, "page" -> page)
			val futureResult = collection.insert(pageWithId)
			Async {
				futureResult.map(_ => Ok(Json.obj("_id" -> objectId)))
			}
		}.recoverTotal { error =>
			BadRequest(Json.obj("res" -> "KO") ++ Json.obj("error" -> JsError.toFlatJson(error)))
		}
	}

	def getAllPagesBySiteId(siteId: Long) = Action {
		Async {
			val cursor: Cursor[JsValue] = collection.find(Json.obj("page.siteId" -> siteId)).cursor[JsValue]
			val futurePagesList: Future[List[JsValue]] = cursor.toList

			futurePagesList.map { pages =>
				Ok(Json.toJson(pages))
			}
		}
	}

	def getPageById(siteId: Long, pageId: String) = Action {
		Async {
			val cursor: Cursor[JsValue] = collection.find(Json.obj("_id" -> BSONObjectID(pageId), "page.siteId" -> siteId)).cursor[JsValue]
			val futurePage: Future[Option[JsValue]] = cursor.headOption

			futurePage.map { page =>
				Ok(Json.toJson(page))
			}
		}
	}

	def getPageByUri(siteId: Long, uri: String) = Action {
		Async {
			val cursor: Cursor[JsValue] = collection.find(Json.obj("page.uri" -> uri, "page.siteId" -> siteId)).cursor[JsValue]
			val futurePage: Future[Option[JsValue]] = cursor.headOption

			futurePage.map { page =>
				Ok(Json.toJson(page))
			}
		}
	}

	def updatePage(siteId: Long, pageId: String) = Action(parse.json) { request =>
		request.body.validate[PageMongo].map { page =>
			val pageWithId = Json.obj("_id" -> BSONObjectID(pageId), "page" -> page)
			val futureResult = collection.save(pageWithId)
			Async {
				futureResult.map(_ => Ok(Json.obj("res" -> "Ok")))
			}
		}.recoverTotal { error =>
			BadRequest(Json.obj("res" -> "KO") ++ Json.obj("error" -> JsError.toFlatJson(error)))
		}
	}
	
	def notAuthorized = BadRequest(Json.obj("error" -> "not authorized"))

}