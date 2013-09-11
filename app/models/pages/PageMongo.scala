package models.pages

import play.api.Logger
import reactivemongo.bson.BSONObjectID
import play.api.libs.json._
import play.api.data.Forms._

case class PageMongo(siteId: Long, uri: String, title: String, parent: String, rows: List[Row])
case class PageMongoWithId(_id: JsValue, page: PageMongo)

object JsonFormats {

	implicit val pageFormat = Json.format[PageMongo]
	implicit val pageWrapperFormat = Json.format[PageMongoWithId]

}