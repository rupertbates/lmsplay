package models

object GameState extends Enumeration {
  type GameState = Value
  val New, Started, Complete = Value
}
