package controllers

import com.codahale.jerkson.Json
import repositories.{MatchRepository}
import services.MatchService
import play.api.mvc.{AnyContent, Action, Controller}
import models.Match

object MatchController extends Controller {
  def matches: Action[AnyContent] = {
    val matches = MatchRepository.find(null).toList
    Action {
      Ok(views.html.matches(matches))
    }
  }
  def listMatches = Action{
      val matches = MatchRepository.find(null).toList
      matches.foreach(m => MatchRepository.save(m))
      Ok(Json.generate(matches))
  }
  def listWeeksMatches(competitionWeek : Int) = Action{
    val matches = MatchService.getMatches(competitionWeek)
    Ok(Json.generate(matches))
  }
}
