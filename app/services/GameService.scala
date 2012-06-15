package services

import org.joda.time.DateTime
import repositories.GameRepository
import org.bson.types.ObjectId
import models.{UserPick, GameRound, Game}
import helpers.CompetitionWeekHelper

object GameService {
  def getGame(id: String, username: String) = {
    val game = GameRepository.findOneById(id)
    game match {
      case None => None
      case Some(g) => {
        if (g.players.contains(username)) {
          game
        }
        else None
      }
    }
  }


  def createGame(name: String, creator: String): Game = {
    val id = new ObjectId().toString
    val game = new Game(id, name, new DateTime(), creator, List(creator), None, List[GameRound]())
    GameRepository.save(game)
    game
  }

  def pickTeam(id: String, competitionWeek: Int, username: String, team: String): Boolean = {
    if (!CompetitionWeekHelper.weekIsEditable(competitionWeek)) return false

    val game = getGame(id, username).getOrElse(return false)

    val round = game.getGameRound(competitionWeek)

    round.userPicks = new UserPick(username, team) :: round.userPicks

    GameRepository.save(game)
    true

  }
}
