import play.api.Application
import play.api.GlobalSettings
import play.api.Logger
import play.api.mvc.RequestHeader
import play.api.mvc.Results.BadRequest
import controllers.LoginController
import scala.concurrent.Future

object Global extends GlobalSettings {

	override def onStart(app: Application) {
		Logger.info("WebsiteBuilder has started")
		LoginController.firstStart
	}

	override def onStop(app: Application) {
		Logger.error("WebsiteBuilder has shut down")
	}

/*	override def onHandlerNotFound(request: RequestHeader): Result = {
		NotFound(views.html.errors.notFound(request.path))
	}*/

	override def onBadRequest(request: RequestHeader, error: String) = {
		Logger.error("Bad Request: "+error)
		Future.successful(BadRequest("Bad Request: "+error))
	}

}



