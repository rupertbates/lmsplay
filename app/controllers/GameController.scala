package controllers

import models.Game
import play.api.data._
import play.api.data.Forms._
import play.api.mvc.Controller
import com.codahale.jerkson.Json
import services.{MatchService, GameService}
import viewmodels.{UserGameModel, ViewGameModel}
import helpers.MatchWeekHelper.getNextMatchWeek
import play.api.Logger
import helpers.MatchWeekHelper

object GameController extends Controller with Secured {

  object HtmlRoutes {
    def viewGame(id: String) = IsAuthenticated {
      username => _ =>
        Logger.debug("Now = " + MatchWeekHelper.now)
        val game = GameService.getGameContainingUser(id, username)
        game match {
          case None => NotFound
          case Some(g) => {
            val nextWeeksMatches = MatchService.getNextWeeksMatchesByDay
            val thisWeeksMatches = MatchService.getThisWeeksMatches
            val userPick = g.getUserPick(getNextMatchWeek, username)
            val model = new ViewGameModel(new UserGameModel(g, username), getNextMatchWeek, nextWeeksMatches, userPick)
            Ok(views.html.gameEmber(Json.generate(model)))
          }
        }
    }

    def index = IsAuthenticated {
      username => _ =>
        Ok(views.html.games(username, Json.generate(GameService.findGamesByUser(username))))
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
        Ok(Json.generate(GameService.findGamesByUser(username)))
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
          GameService.saveGame(game, username)
          Ok(Json.generate(game))
        }
    }

    def pickTeam(id: String, MatchWeek: Int, team: String) = IsAuthenticated {
      username =>
        implicit request => {

          if (GameService.pickTeam(id, MatchWeek, username, team)) {
            Ok("Game updated successfully")
          }else{
            if(MatchWeekHelper.matchesHaveStarted(MatchWeek))
              BadRequest("Couldn't update the game, matches have already started")
            else
              BadRequest("Couldn't update the game")
          }
        }
    }

    def getGame(id: String) = IsAuthenticated {
      username => implicit request =>
        Ok(Json.generate(GameService.getGameContainingUser(id, username).getOrElse("Game not found or User is not in this game")))
    }

    def deleteGame(id: String) = IsAuthenticated {
      username => implicit request =>
        GameService.deleteGame(id, username)
        Ok("success")
    }
  }

}
