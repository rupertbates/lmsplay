package controllers

import helpers.Facebook
import scala.Predef._
import play.api.mvc._


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
//  def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(username, onUnauthorized) {
//    user => Action(request => f(user)(request))
//  }
  def IsAuthenticated[T](parser : BodyParser[T])(f: => String => Request[T] => Result) = Security.Authenticated(username, onUnauthorized) {
    user => Action(parser){request => f(user)(request)}
  }
  def IsAuthenticated( f: => String => Request[AnyContent] => Result) :  Action[(Action[AnyContent], AnyContent)] = IsAuthenticated(play.api.mvc.BodyParsers.parse.anyContent)(f)


}
