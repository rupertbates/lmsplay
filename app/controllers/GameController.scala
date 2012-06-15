package controllers

import models.Game
import play.api.data._
import play.api.data.Forms._
import repositories.GameRepository
import play.api.mvc.Controller
import com.codahale.jerkson.Json
import services.{MatchService, GameService}
import viewmodels.{UserGameModel, ViewGameModel}
import helpers.CompetitionWeekHelper.getNextCompetitionWeek
import play.api.Logger
import helpers.CompetitionWeekHelper

object GameController extends Controller with Secured {

  object HtmlRoutes {
    def viewGame(id: String) = IsAuthenticated {
      username => _ =>
        Logger.debug("Now = " + CompetitionWeekHelper.now)
        val game = GameService.getGame(id, username)
        game match {
          case None => NotFound
          case Some(g) => {
            val nextWeeksMatches = MatchService.getNextWeeksMatchesByDay
            val thisWeeksMatches = MatchService.getThisWeeksMatches
            val userPick = g.getUserPick(getNextCompetitionWeek, username)
            val model = new ViewGameModel(new UserGameModel(g, username), getNextCompetitionWeek, nextWeeksMatches, userPick)
            Ok(views.html.gameEmber(Json.generate(model)))
          }
        }
    }

    def index = IsAuthenticated {
      username => _ =>
        Ok(views.html.games(username, Json.generate(GameRepository.findGamesByUser(username))))
    }

    def newGame = IsAuthenticated {
      username => _ =>
        Ok(views.html.newgame(gameForm("")))
    }

    def gameForm(userName: String) = Form(
      mapping(
        "id" -> nonEmptyText,
        "name" -> nonEmptyText
      )
        ((id, name) => {
          GameService.createGame(name, userName)
        })
        ((game: Game) => Some(game.id.toString, game.name))
    )
  }

  object JsonRoutes {
    def listGames = IsAuthenticated {
      username => _ =>
        Ok(Json.generate(GameRepository.findGamesByUser(username)))
    }

    def createGame = IsAuthenticated {
      username => implicit request =>
        val json = request.body.asJson.get
        val game = GameService.createGame((json \ "name").as[String], username)
        Ok(Json.generate(game))
    }

    def updateGame(id: String) = IsAuthenticated {
      username =>
        implicit request => {
          val json = request.body.asJson.get.toString()
          val game = Json.parse[Game](json)
          GameRepository.save(game)
          Ok(Json.generate(game))
        }
    }

    def pickTeam(id: String, competitionWeek: Int, team: String) = IsAuthenticated {
      username =>
        implicit request => {

          if (GameService.pickTeam(id, competitionWeek, username, team)) {
            Ok("Game updated successfully")
          }else{
            if(CompetitionWeekHelper.weekIsEditable(competitionWeek))
              BadRequest("Couldn't update the game")
            else
              BadRequest("Can't update the user selection for this week as games have already started")
          }


        }
    }

    def getGame(id: String) = IsAuthenticated {
      username => implicit request =>
        Ok(Json.generate(GameRepository.findOneById(id)))
    }

    def deleteGame(id: String) = IsAuthenticated {
      username => implicit request =>
        GameRepository.removeById(id)
        Ok("success")
    }
  }

}
