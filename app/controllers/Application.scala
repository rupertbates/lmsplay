package controllers

import play.api._
import play.api.mvc._
import repositories.MatchRepository
import com.novus.salat._
import com.novus.salat.global._
import com.novus.salat.annotations._
import com.novus.salat.impls._
import com.mongodb.casbah.commons.MongoDBObject

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def matches : Action[AnyContent] = {
    val matches = MatchRepository.find(null).toList
    return Action {
      Ok(views.html.matches(matches))
    }
  }
}