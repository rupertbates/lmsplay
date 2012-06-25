package viewmodels

import models.{MatchWeek, MatchDay, Match, Game}
import org.joda.time.DateTime


case class ViewGameModel(game : UserGameModel, thisMatchWeek : Option[MatchWeek], nextMatchWeek : Option[MatchWeek], userPick : Option[String]) {

}
