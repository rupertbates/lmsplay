package helpers

import org.joda.time.{Interval, DateTime}
import services.MatchService

object CompetitionWeekHelper {

  val dateOffsetMonths = 10
  val startDate = new DateTime(2011, 8, 13, 0, 0)
  def now = new DateTime().minusMonths(dateOffsetMonths)

  def getCompetitionWeek(date : DateTime) : Int = {
    val interval = new Interval(startDate, date)
    val numberOfWeeksFromStart = (interval.toDuration.getStandardDays / 7).asInstanceOf[Int]
    numberOfWeeksFromStart + 1
  }

  def getCurrentCompetitionWeek = {
    getCompetitionWeek(now)
  }
  def getNextCompetitionWeek = getCurrentCompetitionWeek + 1

  def weekIsEditable(week: Int): Boolean = {
    val matches = MatchService.getMatches(week)
    !matches.exists(m => m.kickOffTime.isBefore(now))
  }


}
