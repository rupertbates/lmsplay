package models

import org.joda.time.DateTime

case class MatchDay(day : DateTime, matches : List[Match])
