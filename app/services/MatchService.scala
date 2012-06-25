package services

import repositories.{MatchWeekRepository, MatchRepository}
import org.joda.time.{Interval, DateTimeConstants, DateTime}
import com.mongodb.casbah.commons.MongoDBObject
import models.{MatchWeek, Match, MatchDay}
import com.mongodb.casbah.Imports._

object MatchService {
  implicit def dateTimeOrdering: Ordering[DateTime] = Ordering.fromLessThan(_ isBefore _)
  val dateOffsetMonths = 10
  val startDate = new DateTime(2011, 8, 13, 0, 0)
  def now = new DateTime().minusMonths(dateOffsetMonths).plusWeeks(4)

  def getMatchWeek(date : DateTime) = {
    val q = ("startDate" $lte date) ++ ("endDate" $gte date)
    MatchWeekRepository.findOne(q)
  }

  def getMatchWeek(matchWeek: Int) = {
    //if (matchWeek == 0) MatchWeekRepository.find(null).toList
    MatchWeekRepository.findOne(MongoDBObject("number" -> matchWeek))
  }

  def getCurrentMatchWeek = {
    getMatchWeek(now)
  }

  def getCurrentMatchWeekNumber = {
    getCurrentMatchWeek.map(_.number)
  }
  def getNextMatchWeek = getMatchWeek(now.plusWeeks(1))

  def getMatchWeeks(first : Int, last : Int) = {
    val q = ("number" $gte first $lte last)
    MatchWeekRepository.find(q).toList.sortBy(_.number)
  }
  def getMatchWeeks = {
    MatchWeekRepository.find(null).toList.sortBy(_.number)
  }

}
