package services

import models.Game
import org.joda.time.DateTime
import repositories.GameRepository
import org.bson.types.ObjectId

object GameService {
  def createGame(name : String, creator : String) : Game = {
    val id = new ObjectId().toString
    val game = new Game(id, name, new DateTime(), creator, Some(List(creator)), None, None)
    GameRepository.save(game)
    game
  }

}
