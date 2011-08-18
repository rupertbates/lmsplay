package models

import org.joda.time.DateTime

case class Match(matchWeek : Int, kickOffTime : DateTime, homeTeam : Team,  awayTeam : Team) {
}
