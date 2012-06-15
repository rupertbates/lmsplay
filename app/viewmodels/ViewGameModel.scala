package viewmodels

import models.{Match, Game}


case class ViewGameModel(game : Game, thisWeeksMatches : List[Match], userPick : String = "") {

}
