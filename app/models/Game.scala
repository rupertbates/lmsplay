package models


import models.types.GameRounds
import com.novus.salat.annotations.raw.Key
import java.util.UUID
import org.joda.time.DateTime

case class Game(@Key("_id") id: UUID, name : String, created : DateTime, userIds : List[Int], started : Option[DateTime], gameRounds : Option[Map[Int, Map[String, String]]]) {

}
