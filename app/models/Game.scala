package models


import models.types.GameRounds
import com.novus.salat.annotations.Key
import java.util.UUID
import org.joda.time.DateTime
import org.bson.types.ObjectId
import collection.mutable.ListBuffer

//import scala.collection.mutable.Map
import collection.mutable
import helpers.CompetitionWeekHelper._

case class Game(@Key("_id") id : String,
                name : String,
                created : DateTime = new DateTime(),
                creator : String,
                players : List[String],
                started : Option[DateTime],
                var gameRounds : List[GameRound]) //[gameRoundNumber -> [UserName -> ClubPicked]]
{
  def getUserPick(competitionWeek : Int, username : String) = {
    getGameRound(competitionWeek).userPicks.find(p => p.username == username).getOrElse(new UserPick("", "")).team
  }
  def getGameRound(competitionWeek : Int) = {
    val round = gameRounds.find(g => g.competitionWeek == competitionWeek)
    round match{
      case Some(r) => r
      case None =>
        val r = new GameRound(competitionWeek, List[UserPick]())
        gameRounds = r::gameRounds
        r
    }
  }
}

