package services

import org.joda.time.DateTime
import org.bson.types.ObjectId
import models._
import com.novus.salat.dao.SalatDAO
import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.commons.MongoDBObject
import com.novus.salat.global._
import models.GameRound
import models.UserPick
import models.Game
import play.api.Logger

object GameService {
  def findGamesByUser(username: String) = GameRepository.findGamesByUser(username)

  def getGameContainingUser(id: String, username: String) = {
    GameRepository.findGameContainingUser(id, username)
  }

  def getGameCreatedByUser(id: String, username: String) = {
    GameRepository.findGameCreatedByUser(id, username)
  }

  /**
   * Check and update the 'correct?' flag for every user pick in
   * the given game and week
   * @param game
   * @param matchWeek
   */
  def checkPicks(game: Game, matchWeek: MatchWeek) {
      Logger.debug("checking game picks for id " + game.id + " for week " + matchWeek.number)
      val picks = game.getUserPicks(Some(matchWeek))
      picks.map(p => {
        Logger.debug("got " + p.size + " user picks for week")
        p.map(up => {
          val result = matchWeek.checkTeamResult(up.team)
          Logger.debug(up.username + " picked " + up.team + " and they " + (if (result) "won" else "lost"))
          up.correct = Some(matchWeek.checkTeamResult(up.team))
          GameRepository.save(game)
        })
      })
  }

  /**
   * Check and update the 'correct?' flag for every
   * user pick in every week of the given game
   * @param game
   */
  def checkPicks(game: Game) {
    if (game.started == 0) {
      Logger.debug("game '" + game.name + "' has not started, exiting checkPicks")
    } else {
      Logger.debug("Getting matchWeeks between " + game.started + " and " + MatchService.getCurrentMatchWeekNumber.getOrElse(0))
      val gameWeeks = MatchService.getMatchWeeks(game.started, MatchService.getCurrentMatchWeekNumber.getOrElse(0))
      gameWeeks.map(checkPicks(game, _))
    }
  }

  def checkPicks(id: String) {
    GameRepository.findOneById(id).map(checkPicks(_))
  }

//  def getPlayersStillIn(id: String): Option[List[String]] = {
//    val game = GameRepository.findOneById(id).getOrElse(return None)
//
//    val gameWeeks = MatchService.getMatchWeeks(game.started, MatchService.getCurrentMatchWeekNumber.getOrElse(0))
//
//    None
//  }

  //  def setGameState(f : Unit => Option[Game]){
  //    f() match{
  //      case Some(g) => setGameState(g)
  //      case None => None
  //    }
  //  }
  //  protected def setGameState(game : Game){
  //    game
  //  }

  def saveGame(game: Game, username: String) {
    getGameCreatedByUser(game.id, username).map(g => GameRepository.save(game))
  }

  def deleteGame(id: String, username: String) {
    getGameCreatedByUser(id, username).map(GameRepository.remove(_))
  }

  def createGame(name: String, creator: String): Game = {
    val id = new ObjectId().toString
    val game = new Game(id, name, new DateTime(), creator, List(creator), 0, List[GameRound]())
    GameRepository.save(game)
    game
  }

  def pickTeam(id: String, matchWeek: Int, username: String, team: String): Boolean = {
    if (matchWeek <= MatchService.getCurrentMatchWeekNumber.getOrElse(0))
      return false

    val game = getGameContainingUser(id, username).getOrElse(return false)

    val round = game.getGameRound(matchWeek).getOrElse({
      val r = new GameRound(matchWeek, List[UserPick]())
      game.gameRounds = r::game.gameRounds
      r
    })

    round.userPicks = new UserPick(username, team) :: round.userPicks
    GameRepository.save(game)
    true
  }

  def startGame(id: String, username: String): Boolean = {
    val game = getGameCreatedByUser(id, username).getOrElse(return false)
    val nextWeek = MatchService.getNextMatchWeek.getOrElse(return false)
    game.started = nextWeek.number
    GameRepository.save(game)
    true
  }

  object GameRepository extends SalatDAO[Game, Int](collection = MongoConnection()("test")("game")) {
    def findGamesByUser(username: String) = {
      find(MongoDBObject("players" -> username)).toList
    }

    def findGameCreatedByUser(id: String, username: String) = {
      findOne(MongoDBObject("creator" -> username, "_id" -> id))
    }

    def findGameContainingUser(id: String, username: String) = {
      findOne(MongoDBObject("players" -> username, "_id" -> id))
    }

    def removeById(id: String) = {
      findOneById(id).map(remove(_))
    }

    def findOneById(id: String) = {
      findOne((MongoDBObject("_id" -> id)))
    }
  }

}