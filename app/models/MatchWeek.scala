package models

import org.joda.time.DateTime

case class MatchWeek(number : Int, startDate : DateTime, var endDate : DateTime, var matchDays : List[MatchDay]) {
  def checkTeamResult(pick : String): Boolean = {
    val matches = matchDays.flatten(md => md.matches)
    matches.find(m => m.winningTeam.getOrElse("") == pick).isDefined
  }
}
