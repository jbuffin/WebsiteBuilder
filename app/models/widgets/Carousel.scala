package models.widgets

case class Carousel(images: List[CarouselImage], widgetId: Long)

object Carousel {
	
	def getByWidgetId(widgetId: Long): Option[Carousel] = {
		Option(Carousel(CarouselImage.getByWidgetId(widgetId), widgetId))
	}
	
}