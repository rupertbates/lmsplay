package services

import org.joda.time.DateTime
import repositories.GameRepository
import org.bson.types.ObjectId
import models.{Game}
import collection.mutable.HashMap

object GameService {
  def getGame(id: String, username: String) = {
    val game = GameRepository.findOneById(id)
    game match{
      case None => None
      case Some(g) => {
        if (g.players.contains(username)){
          game
        }
        else None
      }

    }

  }

  def createGame(name : String, creator : String) : Game = {
    val id = new ObjectId().toString
    val game = new Game(id, name, new DateTime(), creator, List(creator), None, Map.empty[Int, Map[String, String]])
    GameRepository.save(game)
    game
  }

}
