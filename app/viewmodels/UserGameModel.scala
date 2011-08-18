package viewmodels

import models.{UserPick, Game}

case class UserGameModel(id: String,
                         name: String,
                         userName: String,
                         players: List[String],
                         picks: List[UserPick]) {
  def this(game: Game, username: String) = this(game.id, game.name, username, game.players, game.getUserPicks(username))
}