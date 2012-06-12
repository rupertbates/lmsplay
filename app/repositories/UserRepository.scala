package repositories

import com.mongodb.casbah.MongoConnection
import com.novus.salat.dao.SalatDAO
import com.novus.salat.global._
import models.{User, Match}
import com.mongodb.casbah.commons.MongoDBObject

object UserRepository extends SalatDAO[User, Int](collection = MongoConnection()("test")("user")) {
  def findByUserName(userName : String) = {
    findOne(MongoDBObject("username" -> userName))
  }

}
