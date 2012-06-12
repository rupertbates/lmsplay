package services

import models.Game
import org.joda.time.DateTime
import repositories.GameRepository

object GameService {
  def createGame(name : String, creator : String) : Game = {
    val game = Game(0, name, new DateTime(), creator, Some(List(creator)), None, None)
    GameRepository.save(game)
    game
  }

}
