package controllers

import scala.concurrent.Future
import models.User
import models.UserFormats.userFormat
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.mvc.Security.AuthenticatedBuilder
import play.api.mvc.Security._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.Logger
import scala.concurrent.Await
import play.api.libs.json.JsValue
import scala.concurrent.duration.Duration
import play.api.libs.concurrent.Execution.Implicits._

object LoginController extends Controller with MongoController {
	def collection: JSONCollection = db.collection[JSONCollection]("login")

	val loginForm = Form(
		tuple(
			"username" -> nonEmptyText,
			"password" -> text
		) verifying ("Invalid username or password", result => result match {
				case (username, password) => {
					val query: JsValue = Json.toJson(User(None, username, None, password))
					val futureUser = collection.find(query).one
					Await.result(futureUser, Duration.Inf).map { user =>
						true
					}.getOrElse {
						false
					}
				}
			})
	)

	def login = Action { implicit request =>
		Ok(views.html.login.login(loginForm))
	}

	def logout = Action {
		Redirect(routes.LoginController.login).withNewSession.flashing(
			"success" -> "You've been logged out"
		)
	}

	def authenticate = Action { implicit request =>
		loginForm.bindFromRequest.fold(
			formWithErrors => BadRequest(views.html.login.login(formWithErrors)),
			user => {
				Logger.debug("Successful Login")
				Redirect(routes.Application.adminHome).withSession("username" -> user._1)
			}
		)
	}

	def firstStart = {
		val futureUserList: Future[Option[User]] = collection.find(Json.obj()).cursor[User].headOption
		futureUserList.map {
			case Some(userList) => Ok
			case None => {
				collection.insert(User(None, "admin", None, "admin")).onComplete(_ => Ok)
			}
		}
	}

}

object Authenticated extends AuthenticatedBuilder(req => req.session.get("username").orElse(Option("")))
