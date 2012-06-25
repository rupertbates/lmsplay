package models


import models.types.GameRounds
import com.novus.salat.annotations.Key
import java.util.UUID
import org.joda.time.DateTime
import org.bson.types.ObjectId
import collection.mutable.ListBuffer

case class Game(@Key("_id") id : String,
                name : String,
                created : DateTime = new DateTime(),
                creator : String,
                players : List[String],
                var started : Int,
                var gameRounds : List[GameRound])
{
  val NEW = 0
  val STARTED = 1
  val COMPLETE = 2

  def getUserPick(matchWeek : Option[MatchWeek], username : String) = {
    matchWeek
      .flatMap(mw => getGameRound(mw.number))
      .flatMap(gr => gr.userPicks.find(p => p.username == username))
      .map(_.team)
  }
  def getUserPicks(matchWeek : Option[MatchWeek]) = {
    matchWeek.flatMap(mw => getGameRound(mw.number))
      .map(_.userPicks)
  }
  def getUserRounds(username : String) = {
    gameRounds.map(r => new GameRound(r.matchWeek, r.userPicks.filter(p => p.username == username)))
  }
  def getGameRound(matchWeek : Int) = {
    gameRounds.find(g => g.matchWeek == matchWeek)
//    round match{
//      case Some(r) => r
//      case None =>
//        val r = new GameRound(matchWeek, List[UserPick]())
//        gameRounds = r::gameRounds
//        r
//    }
  }
}

