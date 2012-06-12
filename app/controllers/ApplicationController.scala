package controllers

import play.api.mvc._
import play.api.libs.concurrent._
import play.api.Play.current
import repositories.MatchRepository
import helpers.Facebook
import services.UserService

object ApplicationController extends Controller {

  def index = Action {
    request =>
      Ok("Hello ")
  }

  def matches: Action[AnyContent] = {
    val matches = MatchRepository.find(null).toList
    Action {
      Ok(views.html.matches(matches))
    }
  }

  def facebookAuthCallback = Action {
    request =>
    //check for cross site request forgery
      if (request.queryString("state")(0) != request.session("state"))
        BadRequest("Cross site request forgery")
      else {
        val code = request.queryString("code")(0)

        val promiseOfUser = Akka.future {
          UserService.authenticate(code)
        }
        Async {
          promiseOfUser.map(option =>
            option match {
              case None => Redirect(routes.ApplicationController.index).withNewSession.flashing("Login failed" -> "Login failure")
              case Some(user) => Redirect(routes.GamesController.index).withSession(("username" -> user.username), ("userId" -> user.id.toString))
            })
        }
      }
  }

  /**
   * Logout and clean the session.
   */
  def logout = Action {
    Redirect(routes.ApplicationController.index).withNewSession.flashing(
      "success" -> "You've been logged out"
    )
  }
}


