package models

import play.api.libs.json.JsValue

case class SiteMongo(siteName: String, hostName: String, siteId: Long = -1)

object SiteMongoFormats {
	import play.api.data._
	import play.api.data.Forms._
	import play.api.libs.json.Json
	
	implicit val siteMongoFormat = Json.format[SiteMongo]
}