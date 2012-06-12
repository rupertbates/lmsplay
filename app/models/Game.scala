package models


import models.types.GameRounds
import com.novus.salat.annotations.raw.Key
import java.util.UUID
import org.joda.time.DateTime

case class Game(@Key("_id") id: Int,
                name : String,
                created : DateTime = new DateTime(),
                creator : String,
                players : Option[List[String]],
                started : Option[DateTime],
                gameRounds : Option[Map[Int, Map[String, String]]]) //[gameRoundNumber -> [UserName -> ClubPicked]]


