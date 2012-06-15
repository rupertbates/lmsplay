package models

import org.joda.time.DateTime

case class Match(competitionWeek : Int, kickOffTime : DateTime, homeTeam : Team,  awayTeam : Team) {
}
