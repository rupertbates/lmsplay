package controllers

import models.{Game, User, Team}
import play.api.mvc.BodyParsers.parse
import play.api.data._
import play.api.data.Forms._
import java.util.UUID
import org.joda.time.DateTime
import repositories.GameRepository
import play.api.mvc.{Request, Controller}
import com.codahale.jerkson.Json
import play.api.libs.json.Json.toJson
import services.{UserService, GameService}
import org.bson.types.ObjectId
import com.codahale.jerkson.Json.parse

object GamesController extends Controller with Secured {
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


  def index = IsAuthenticated {
    username => _ =>
        Ok(views.html.games(Json.generate(GameRepository.findGamesByUser(username))))
  }

  def listGames = IsAuthenticated {
    username => _ =>
      Ok(Json.generate(GameRepository.findGamesByUser(username)))
  }

  def newGame = IsAuthenticated {
    username => _ =>
      Ok(views.html.newgame(gameForm("")))
  }

  def createGame = IsAuthenticated{
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

  def getGame(id: String) = IsAuthenticated {
    username => implicit request =>
      Ok("test")
  }

  def deleteGame(id: String) = IsAuthenticated {
    username => implicit request =>
      GameRepository.removeById(id)
      Ok("success")
  }
}
