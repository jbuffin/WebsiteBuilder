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
import play.api.data.Form
import models._
import models.pages.JsonFormats._

object SitesController extends Controller with MongoController {

	def collection: JSONCollection = db.collection[JSONCollection]("pages")

}