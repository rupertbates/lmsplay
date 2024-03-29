package controllers

import play.api.mvc._
import play.api.libs.concurrent._
import play.api.Play.current
import repositories.MatchRepository
import helpers.Facebook
import services.UserService
import admin.ImportMatches

object ApplicationController extends Controller {

  def index = Action {
    implicit request =>
      session.get("username") match {
        case None => Ok(views.html.index("Hello"))
        case Some(user) => Ok(views.html.loggedInHome(user))
      }
  }

  def importMatches = Action {
    ImportMatches.importMatches()
    Redirect(routes.MatchController.matches())
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
              case Some(user) => Redirect(routes.GameController.HtmlRoutes.index).withSession(("username" -> user.username.replace('.', '_')), ("userId" -> user.id.toString))
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


