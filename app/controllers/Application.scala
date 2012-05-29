package controllers

import play.api._
import libs.concurrent.Promise
import libs.ws
import libs.ws.WS
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.libs.concurrent._
import com.codahale.jerkson.Json._
import models._
import views._
import repositories.MatchRepository
import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.annotations._
import com.novus.salat.impls._
import com.mongodb.casbah.commons.MongoDBObject
import helpers.Facebook

object Application extends Controller {
  // -- Authentication
  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ) verifying("Invalid email or password", result => result match {
      case (email, password) => User.authenticate(email, password)
    })
  )


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
      //Async {
        val code = request.queryString("code")(0)
        val user = Facebook.getUser(code)
        Ok(user.name)
        /*val url = Facebook.AccessTokenBaseUrl + code
        val access = WS.url(url).get
        val reponse = access.await(5000).get
        WS.url(url).get().flatMap(response => {
          val accessToken = Facebook.parseAccessTokenResponse(response.body)
          WS.url(Facebook.UserDetailsBaseUrl + accessToken._1).get().map(response2 => {
            try {
              val user = com.codahale.jerkson.Json.parse[User](response2.body)
              Ok(response2.body)
            } catch {
              case e: Exception => {
                Logger.error(e.getMessage)
                Ok(response2.body)
              }
            }
          })
        }
        )*/
      //}
  }


  /**
   * Login page.
   */
  def login = Action {
    implicit request =>
      Ok(views.html.login(loginForm))
  }


  /**
   * Handle login form submission.
   */
  def authenticate = Action {
    implicit request =>
      loginForm.bindFromRequest.fold(
        formWithErrors => BadRequest(html.login(formWithErrors)),
        user => Redirect(routes.Games.index).withSession("email" -> user._1)
      )
  }

  /**
   * Logout and clean the session.
   */
  def logout = Action {
    Redirect(routes.Application.login).withNewSession.flashing(
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
  private def username(request: RequestHeader) = request.session.get("email")

  /**
   * Redirect to login if the user in not authorized.
   */
  private def onUnauthorized(request: RequestHeader) = Results.Redirect(Facebook.AuthenticateBaseUrl + java.util.UUID.randomUUID())

  // --

  /**
   * Action for authenticated users.
   */
  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) {
    user =>
      Action(request => f(user)(request))
  }

  //  /**
  //   * Check if the connected user is a member of this project.
  //   */
  //  def IsMemberOf(project: Long)(f: => String => Request[AnyContent] => Result) = IsAuthenticated { user => request =>
  //    if(Project.isMember(project, user)) {
  //      f(user)(request)
  //    } else {
  //      Results.Forbidden
  //    }
  //  }
  //
  //  /**
  //   * Check if the connected user is a owner of this task.
  //   */
  //  def IsOwnerOf(task: Long)(f: => String => Request[AnyContent] => Result) = IsAuthenticated { user => request =>
  //    if(Task.isOwner(task, user)) {
  //      f(user)(request)
  //    } else {
  //      Results.Forbidden
  //    }
  //  }

}
