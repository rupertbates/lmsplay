package models


import models.types.GameRounds
import com.novus.salat.annotations.Key
import java.util.UUID
import org.joda.time.DateTime
import org.bson.types.ObjectId
import collection.mutable.ListBuffer

//import scala.collection.mutable.Map
import collection.mutable
import helpers.MatchWeekHelper._
object GameState extends Enumeration {
  type GameState = Value
  val New, Started, Complete = Value
}
case class Game(@Key("_id") id : String,
                name : String,
                created : DateTime = new DateTime(),
                creator : String,
                players : List[String],
                started : Option[DateTime],
                var gameRounds : List[GameRound])
{
  def getUserPick(MatchWeek : Int, username : String) = {
    getGameRound(MatchWeek).userPicks.find(p => p.username == username).getOrElse(new UserPick("", "")).team
  }
  def getUserPicks(username : String) = {
    gameRounds.flatten(r => r.userPicks.filter(p => p.username == username))
  }
  def getGameRound(matchWeek : Int) = {
    val round = gameRounds.find(g => g.matchWeek == matchWeek)
    round match{
      case Some(r) => r
      case None =>
        val r = new GameRound(matchWeek, List[UserPick]())
        gameRounds = r::gameRounds
        r
    }
  }
//  def getState = {
//    if(started.isEmpty)
//      GameState.New
//    else if
//      playersIn.
//  }
}

