package helpers

import org.joda.time.{Interval, DateTime}

object CompetitionWeekHelper {
  val startDate = new DateTime(2011, 8, 13, 0, 0)
  def getCompetitionWeek(date : DateTime) : Int = {
    val interval = new Interval(startDate, date)
    val numberOfWeeksFromStart = (interval.toDuration.getStandardDays / 7).asInstanceOf[Int]
    numberOfWeeksFromStart + 1
  }

}
