package services

import repositories.MatchRepository
import org.joda.time.{DateTimeConstants, DateTime}

object MatchService {
  val dateOffsetMonths = 10

  def getThisWeeksMatches(plusWeeks : Int = 0) = {
    val today = new DateTime().minusMonths(dateOffsetMonths).plusWeeks(plusWeeks)
    val roundStart = getRoundStart(today)
    val roundEnd = roundStart.plusWeeks(1)
    val matches = MatchRepository.find(null).toList
    matches.filter(m => m.kickOffTime.isAfter(roundStart) && m.kickOffTime.isBefore(roundEnd))
  }
  def getNextWeeksMatches = {
    getThisWeeksMatches(1)
  }

  def getRoundStart(d: DateTime) = {
    if (d.getDayOfWeek > DateTimeConstants.THURSDAY) {
      d.withDayOfWeek(DateTimeConstants.THURSDAY)
    } else {
      d.plusWeeks(-1).withDayOfWeek(DateTimeConstants.THURSDAY)
    }

  }

}
