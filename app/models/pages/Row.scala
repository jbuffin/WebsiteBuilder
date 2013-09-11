package models.pages

import play.api.libs.json._
import play.api.data.Forms._

case class Row(order: Long, columns: List[Column])

object Row extends Function2[Long, List[Column], Row] {
	implicit val rowFormat = Json.format[Row]
}