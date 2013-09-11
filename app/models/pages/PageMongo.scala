package models.pages

import play.api.Logger
import reactivemongo.bson.BSONObjectID
import play.api.libs.json._
import play.api.data.Forms._

case class PageMongo(siteId: Long, uri: String, title: String, parent: String, rows: List[Row])

case class Row(order: Long, columns: Option[List[Column]])

case class Column(order: Long, columnHtml: String)

case class PageMongoWithId(_id: JsValue, page: PageMongo)

object JsonFormats {

	implicit val columnFormat = Json.format[Column]
	implicit val rowFormat = Json.format[Row]
	implicit val pageFormat = Json.format[PageMongo]
	implicit val pageWrapperFormat = Json.format[PageMongoWithId]

}