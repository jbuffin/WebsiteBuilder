package models.pages

import play.api.libs.json._
import play.api.data.Forms._
import play.api.libs.functional.syntax._

case class Column(order: Long, columnHtml: Option[String], rowsOption: Option[List[Row]])

object Column extends Function3[Long, Option[String], Option[List[models.pages.Row]], Column] {
	implicit val columnFormat: Format[Column] = Json.format[Column]
}