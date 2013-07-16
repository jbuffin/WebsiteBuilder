import play.api.Application
import play.api.GlobalSettings
import play.api.Logger
import play.api.mvc.RequestHeader
import play.api.mvc.Results.BadRequest

object Global extends GlobalSettings {

	override def onStart(app: Application) {
		Logger.info("ChurchSite has started")
	}

	override def onStop(app: Application) {
		Logger.error("ChurchSite has shut down")
	}

/*	override def onHandlerNotFound(request: RequestHeader): Result = {
		NotFound(views.html.errors.notFound(request.path))
	}*/

	override def onBadRequest(request: RequestHeader, error: String) = {
		Logger.error("Bad Request: "+error)
		BadRequest("Bad Request: "+error)
	}

}



