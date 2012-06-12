package repositories

import com.mongodb.casbah.MongoConnection
import com.novus.salat.dao.SalatDAO
import com.novus.salat.global._
import models.{Game, User}

object GameRepository extends SalatDAO[Game, Int](collection = MongoConnection()("test")("game")) {

}
