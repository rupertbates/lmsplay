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
              case Some(user) => Redirect(routes.GamesController.index).withSession("username" -> user.username)
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

/**
 * Provide security features
 */
trait Secured {

  /**
   * Retrieve the connected user email.
   */
  private def username(request: RequestHeader) = request.session.get("username")

  /**
   * Redirect to facebook if the user is not authorized.
   */
  private def onUnauthorized(request: RequestHeader) = {
    val state = java.util.UUID.randomUUID().toString
    Results.Redirect(Facebook.AuthenticateBaseUrl + state).withSession("state" -> state)
  }

  // --

  /**
   * Action for authenticated users.
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) {
    user => Action(request => f(user)(request))
  }

}
