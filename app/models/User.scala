package models

import play.api.libs.json.JsValue

case class User(_id: Option[JsValue], username: String, email: Option[String], password: String, siteId: Option[Long] = None)

object UserFormats {
	import play.api.data._
	import play.api.data.Forms._
	import play.api.libs.json.Json
	
	implicit val userFormat = Json.format[User]
}