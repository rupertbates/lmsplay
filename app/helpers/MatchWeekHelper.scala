package helpers

import org.joda.time.{Interval, DateTime}
import services.MatchService

object MatchWeekHelper {

  val dateOffsetMonths = 0
  val startDate = new DateTime(2011, 8, 13, 0, 0)
  def now = new DateTime().minusMonths(dateOffsetMonths)

  def getMatchWeek(date : DateTime) : Int = {
    val interval = new Interval(startDate, date)
    val numberOfWeeksFromStart = (interval.toDuration.getStandardDays / 7).asInstanceOf[Int]
    numberOfWeeksFromStart + 1
  }

  def getCurrentMatchWeek = {
    getMatchWeek(now)
  }
  def getNextMatchWeek = getCurrentMatchWeek + 1

  def matchesHaveStarted(week: Int): Boolean = {
    val matches = MatchService.getMatches(week)
    !matches.exists(m => m.kickOffTime.isBefore(now))
  }


}
