package controllers

import play.api._
import play.api.mvc._
import helpers.CouchHelper

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def matches = Action {
    Ok(views.html.matches(CouchHelper.getGames()))
  }
  
}