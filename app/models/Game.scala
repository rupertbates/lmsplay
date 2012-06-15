package models


import models.types.GameRounds
import com.novus.salat.annotations.Key
import java.util.UUID
import org.joda.time.DateTime
import org.bson.types.ObjectId

case class Game(@Key("_id") id : String,
                name : String,
                created : DateTime = new DateTime(),
                creator : String,
                players : List[String],
                started : Option[DateTime],
                gameRounds : Map[Int, Map[String, String]]) //[gameRoundNumber -> [UserName -> ClubPicked]]


