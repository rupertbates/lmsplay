package models

import org.joda.time.DateTime

case class MatchWeek(number : Int, startDate : DateTime, var endDate : DateTime, var matchDays : List[MatchDay])
