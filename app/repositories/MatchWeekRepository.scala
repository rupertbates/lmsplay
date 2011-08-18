package repositories

import com.novus.salat.dao.SalatDAO
import com.novus.salat.global._
import models.{MatchWeek, Match}
import com.mongodb.casbah.MongoConnection

object MatchWeekRepository extends SalatDAO[MatchWeek, Int](collection = MongoConnection()("test")("matchWeek")) {

}
