package repositories

import com.mongodb.casbah.MongoConnection
import com.novus.salat.dao.SalatDAO
import com.novus.salat.global._
import models.Match

object MatchRepository extends SalatDAO[Match, Int](collection = MongoConnection()("test")("match")) {

}
