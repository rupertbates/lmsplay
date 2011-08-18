package viewmodels

import models.{MatchDay, Match, Game}
import org.joda.time.DateTime


case class ViewGameModel(game : UserGameModel, matchWeek : Int, nextWeeksMatches : List[MatchDay], userPick : String = "") {

}
