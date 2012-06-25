package viewmodels

import models.{GameRound, UserPick, Game}

case class UserGameModel(id: String,
                         name: String,
                         userName: String,
                         started: Int,
                         players: List[String],
                         rounds: List[GameRound]) {
  def this(game: Game, username: String) = this(game.id, game.name, username, game.started, game.players, game.getUserRounds(username))
}