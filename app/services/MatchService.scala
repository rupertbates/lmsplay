package services

import repositories.MatchRepository
import org.joda.time.{DateTimeConstants, DateTime}
import com.mongodb.casbah.commons.MongoDBObject
import helpers.CompetitionWeekHelper.getCurrentCompetitionWeek
import models.{Match, MatchDay}

object MatchService {


  def getMatches(competitionWeek : Int) = {
    MatchRepository.find(MongoDBObject("competitionWeek" -> competitionWeek)).toList
  }
  def getMatchDays(competitionWeek : Int) = getMatches(competitionWeek)
    .groupBy(m => new DateTime(m.kickOffTime.toDateMidnight))
    .map(args => new MatchDay(args._1, args._2))
    .toList
  def getThisWeeksMatches = getMatches(getCurrentCompetitionWeek)
  def getNextWeeksMatches = getMatches(getCurrentCompetitionWeek + 1)
  def getThisWeeksMatchesByDay = getMatchDays(getCurrentCompetitionWeek)
  def getNextWeeksMatchesByDay = getMatchDays(getCurrentCompetitionWeek + 1)



//  def getRoundStart(d: DateTime) = {
//    if (d.getDayOfWeek > DateTimeConstants.THURSDAY) {
//      d.withDayOfWeek(DateTimeConstants.THURSDAY)
//    } else {
//      d.plusWeeks(-1).withDayOfWeek(DateTimeConstants.THURSDAY)
//    }
//
//  }

}
