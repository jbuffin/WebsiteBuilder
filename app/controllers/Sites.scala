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
import models.User
import models.UserFormats.userFormat
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import play.api.libs.concurrent.Execution.Implicits._

object Sites extends Controller with MongoController {
  def collection: JSONCollection = db.collection[JSONCollection]("pages")
  def loginCollection: JSONCollection = db.collection[JSONCollection]("login")

  def getPageFromUri(uri: String = "", editable: Boolean = false) = Authenticated.async {
    implicit request => {
      val username = request.user
      Logger.debug("[Sites.getPageFromUri]: request.domain: '"+request.domain+"', uri: '"+uri+"'")
      val site = Site.getSiteByHostName(request.domain).get
      val futureUser = loginCollection.find(Json.obj("username" -> username)).one[User]
      val isAdmin = Await.result(futureUser, Duration.Inf).map { siteAdminuser =>
        Logger.debug("found a user: "+siteAdminuser)
        Logger.debug(siteAdminuser.siteId.getOrElse(true).toString+" == "+site.siteId)
        Logger.debug((siteAdminuser.siteId.getOrElse(true) == site.siteId).toString)
        siteAdminuser.siteId.getOrElse(site.siteId) == site.siteId
      }.getOrElse {
        Logger.debug("no user found")
        false
      }
      Logger.debug(username)
      getPageFromSiteIdAndUri(site.siteId, uri, isAdmin)
    }
  }

  def getPageFromSiteIdAndUri(siteId: Long, uri: String = "", isAdmin: Boolean): Future[play.api.mvc.Result] = {
    Logger.debug("[Sites.getPageFromSiteIdAndUri]: siteId: '"+siteId+"', uri: '"+uri+"'")
    val query = Json.obj("page.uri" -> uri, "page.siteId" -> siteId)
    val futurePage = collection.find(query).one[PageMongoWithId]
    futurePage.map { pageMongo =>
      Ok(views.html.sites.page(Site.getSiteById(siteId).get.siteName, pageMongo.get, isAdmin))
    }
  }

  def goToPage(site: Site, pageMongo: PageMongoWithId, isAdmin: Boolean) = {
    Ok(views.html.sites.page(site.siteName, pageMongo, isAdmin))
  }

  def getNavigationBySiteId(siteId: Long): Future[List[PageMongoWithId]] = {
    collection.find(Json.obj("page.siteId" -> siteId)).cursor[PageMongoWithId].collect[List]()
  }

}
