package models

case class UserPick(username : String, team : String, var correct : Option[Boolean] = None)
