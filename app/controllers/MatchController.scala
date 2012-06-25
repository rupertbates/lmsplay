package controllers

import com.codahale.jerkson.Json
import repositories.{MatchRepository}
import services.MatchService
import play.api.mvc.{AnyContent, Action, Controller}
import models.Match
import admin.ImportMatches

object MatchController extends Controller {
  def matches: Action[AnyContent] = {
    val matches = MatchService.getMatchWeeks
    Action {
      Ok(views.html.matches(matches))
    }
  }
  def listMatches = Action{
      val matches = ImportMatches.loadGames() // MatchRepository.find(null).toList
      //matches.foreach(m => MatchRepository.save(m))
      Ok(Json.generate(matches))
  }
  def listWeeksMatches(weekNumber : Int) = Action{
    val matchWeek = MatchService.getMatchWeek(weekNumber)
    Ok(Json.generate(matchWeek))
  }
}
