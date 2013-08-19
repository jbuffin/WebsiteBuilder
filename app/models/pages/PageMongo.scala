package models.pages

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import play.api.Logger
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.templates.Html
import reactivemongo.bson.BSONObjectID

case class PageMongo(siteId: Long, uri: String, title: String, parent: String, rows: List[Row])

case class Row(order: Long, columns: List[Column])

case class Column(order: Long, columnHtml: String)

case class PageMongoWithId(_id: JsValue, page: PageMongo)

object JsonFormats {
	import play.api.libs.json.Json
	import play.api.data._
	import play.api.data.Forms._

	implicit val columnFormat = Json.format[Column]
	implicit val rowFormat = Json.format[Row]
	implicit val pageFormat = Json.format[PageMongo]
	implicit val pageWrapperFormat = Json.format[PageMongoWithId]

}