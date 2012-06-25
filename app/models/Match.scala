package models

import org.joda.time.DateTime

case class Match(matchWeek : Int, kickOffTime : DateTime, homeTeam : Team,  awayTeam : Team) {
  def winningTeam : Option[Team] = {
    if (homeTeam.score > awayTeam.score) homeTeam
    else if (awayTeam.score > homeTeam.score) awayTeam
    None
  }
}
