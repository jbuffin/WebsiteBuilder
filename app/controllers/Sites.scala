package controllers

import play.api._
import play.api.mvc._

object Sites extends Controller {
	
	def index(site: String) = Action {
		Ok(views.html.sites.index(site))
	}
	
}