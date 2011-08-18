package models

object GameErrors extends Enumeration {
  type GameErrors = Value
  val NotFound , NotFoundOrInvalidUser, GamesStarted , Success = Value
}
