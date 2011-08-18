package services

import org.joda.time.DateTime
import org.bson.types.ObjectId
import models.{GameErrors, UserPick, GameRound, Game}
import helpers.MatchWeekHelper
import com.novus.salat.dao.SalatDAO
import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat.global._

object GameService {
  def findGamesByUser(username: String) = GameRepository.findGamesByUser(username)

  def getGameContainingUser(id: String, username: String) = {
    GameRepository.findGameContainingUser(id, username)
  }
  def getGameCreatedByUser(id: String, username: String) = {
    GameRepository.findGameCreatedByUser(id, username)
  }
  def setGameState(f : Unit => Option[Game]){
    f() match{
      case Some(g) => setGameState(g)
      case None => None
    }
  }
  protected def setGameState(game : Game){
    game
  }

  def saveGame(game: Game, username: String) {
    getGameCreatedByUser(game.id, username).map(g => GameRepository.save(game))
  }
  def deleteGame(id: String, username: String) {
    getGameCreatedByUser(id, username).map(GameRepository.remove(_))
  }

  def createGame(name: String, creator: String): Game = {
    val id = new ObjectId().toString
    val game = new Game(id, name, new DateTime(), creator, List(creator), None, List[GameRound]())
    GameRepository.save(game)
    game
  }

  def pickTeam(id: String, matchWeek: Int, username: String, team: String): Boolean = {
    if (!MatchWeekHelper.matchesHaveStarted(matchWeek)) return false

    val game = getGameContainingUser(id, username).getOrElse(return false)

    val round = game.getGameRound(matchWeek)

    round.userPicks = new UserPick(username, team) :: round.userPicks

    GameRepository.save(game)
    true
  }

  protected object GameRepository extends SalatDAO[Game, Int](collection = MongoConnection()("test")("game")) {
    def findGamesByUser(username: String) = {
      find(MongoDBObject("players" -> username)).toList
    }

    def findGameCreatedByUser(id : String, username: String) = {
      findOne(MongoDBObject("creator" -> username, "id" -> id))
    }

    def findGameContainingUser(id : String, username : String ) = {
      findOne(MongoDBObject("players" -> username, "id" -> id))
    }

    def removeById(id: String) = {
      findOneById(id).map(remove(_))
    }

    def findOneById(id: String) = {
      findOne((MongoDBObject("_id" -> id)))
    }
  }
}