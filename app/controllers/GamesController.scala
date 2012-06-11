package controllers

import play.api.mvc.Controller
import models.{User}

object GamesController extends Controller with Secured{
  def index = IsAuthenticated { username => _ =>
    Ok(views.html.games("games page " + username))
  }

}
