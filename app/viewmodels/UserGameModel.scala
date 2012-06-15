package viewmodels

import models.{UserPick, Game}

case class UserGameModel(name: String,
                         userName : String,
                         players : List[String],
                         picks : List[UserPick])
{
  def this(game : Game, username : String) = this(game.name, username, game.players, game.getUserPicks(username))
}