import models._
import controllers._
import anorm._
import play.api.db.DB
import play.api.GlobalSettings
import play.api.Play._
import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.Application
import controllers.DefaultDataInit

object Global extends GlobalSettings {

	override def onStart(app: Application) {
		Logger.info("Initializing default data...")
		DefaultDataInit.insert()
		Logger.info("...done")
		Logger.info("ChurchSite has started")
	}

	override def onStop(app: Application) {
		Logger.info("ChurchSite has shut down")
	}

/*	override def onHandlerNotFound(request: RequestHeader): Result = {
		NotFound(views.html.errors.notFound(request.path))
	}*/

	override def onBadRequest(request: RequestHeader, error: String) = {
		Logger.error("Bad Request: "+error)
		BadRequest("Bad Request: "+error)
	}

}



