package services

import repositories.{MatchWeekRepository, MatchRepository}
import org.joda.time.{DateTimeConstants, DateTime}
import com.mongodb.casbah.commons.MongoDBObject
import helpers.MatchWeekHelper.getCurrentMatchWeek
import models.{MatchWeek, Match, MatchDay}

object MatchService {

  implicit def dateTimeOrdering: Ordering[DateTime] = Ordering.fromLessThan(_ isBefore _)

  def groupMatchesByDay(matches: List[Match]) = {
    matches
      .groupBy(m => new DateTime(m.kickOffTime.toDateMidnight))
      .map(args => new MatchDay(args._1, args._2))
      .toList
      .sortBy(m => m.day)
  }

  def getMatches(matchWeek: Int) = {
    if (matchWeek == 0) MatchRepository.find(null).toList
    else MatchRepository.find(MongoDBObject("matchWeek" -> matchWeek)).toList
  }

  def getMatchDays(matchWeek: Int = 0) =
    groupMatchesByDay(getMatches(matchWeek))

  def getThisWeeksMatches = getMatches(getCurrentMatchWeek)

  def getNextWeeksMatches = getMatches(getCurrentMatchWeek + 1)

  def getThisWeeksMatchesByDay = getMatchDays(getCurrentMatchWeek)

  def getNextWeeksMatchesByDay = getMatchDays(getCurrentMatchWeek + 1)

  def getMatchWeeks = {
//    getMatches(0)
//      .groupBy(m => m.matchWeek)
//      .map(args => new MatchWeek(args._1, args._2(0).kickOffTime, args._2(0).kickOffTime, groupMatchesByDay(args._2)))
//      .toList
//      .sortBy(_.number)

    MatchWeekRepository.find(null).toList.sortBy(_.number)

  }

}
