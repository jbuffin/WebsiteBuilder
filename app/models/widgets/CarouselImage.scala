package models.widgets

import play.api._
import play.api.db._
import play.api.libs._
import anorm._
import anorm.SqlParser._

case class CarouselImage(
	image: String,
	title: String,
	text: String)

object CarouselImage {

/*	val simple = {
		get[String]("sites.site_name") ~
			get[String]("sites.hostname") ~
			get[Long]("sites.site_id") map {
				case site_name ~ hostname ~ site_id => Site(site_name, hostname, site_id)
			}
	}*/
	
}