package models.widgets

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.SqlParser._

case class CarouselImage(
	image: String,
	title: String,
	text: String,
	id: Long = -1)

object CarouselImage {

	val simple = {
		get[String]("carousel_image.image") ~
			get[String]("carousel_image.title") ~
			get[String]("carousel_image.text") ~
			get[Long]("carousel_image.carousel_image_id") map {
				case image ~ title ~ text ~ id => CarouselImage(image, title, text, id)
			}
	}
	
	def getById(carouselImageId: Long): Option[CarouselImage] = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * from carousel_image
						where carousel_image_id = {carousel_image_id}
				"""
			).on(
				'carousel_image_id -> carouselImageId
			).as(CarouselImage.simple.singleOpt)
		}
	}
	
	def getByWidgetId(widgetId: Long) = {
		DB.withConnection { implicit connection =>
			SQL(
				"""
					select * from carousel_image
						where widget_id = {widget_id}
				"""
			).on(
				'widget_id -> widgetId
			).as(CarouselImage.simple*)
		}
	}
	
}