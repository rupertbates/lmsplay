package repositories

import com.mongodb.casbah.MongoConnection
import com.novus.salat.dao.SalatDAO
import com.novus.salat.global._
import models.{Game, User}
import com.mongodb.casbah.commons.MongoDBObject

object GameRepository extends SalatDAO[Game, Int](collection = MongoConnection()("test")("game")) {
  def findGamesByUser(username : String) = {
    find(MongoDBObject("players" -> username)).toList
  }
  def removeById(id : String) = {
    findOneById(id).map(remove(_))
  }
  def findOneById(id : String) = {
    findOne((MongoDBObject("_id" -> id)))
  }
}
