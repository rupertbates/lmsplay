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

object GamesController extends Controller with Secured {
  //  val teamForm: Form[Team] = Form(
  //    mapping(
  //      "name" -> text,
  //      "score" -> number
  //    )(Team.apply)(Team.unapply)
  //  )
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

  def newGame = IsAuthenticated {
    username => _ =>
      Ok(views.html.newgame(gameForm("")))
  }

  def createGame = IsAuthenticated{
    username => implicit request =>

      val json = request.body.asJson.get
      val game = GameService.createGame((json \ "name").as[String], username)

      Ok("test")
  }

  def updateGame(id: Int) = IsAuthenticated {
    username =>
      implicit request =>
        gameForm(username).bindFromRequest.fold(
          formWithErrors => BadRequest,
          value => {
            GameRepository.insert(value)
            Redirect(routes.GamesController.index())
          })
  }

  def getGame(id: Int) = IsAuthenticated {
    username => implicit request =>
      Ok("test")
  }

  def deleteGame(id: Int) = IsAuthenticated {
    username => implicit request =>
      GameRepository.removeById(id)
      Ok("success")
  }
}
