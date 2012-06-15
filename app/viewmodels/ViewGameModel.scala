package viewmodels

import models.{Match, Game}


case class ViewGameModel(game : Game, competitionWeek : Int, thisWeeksMatches : List[Match], userPick : String = "") {

}
