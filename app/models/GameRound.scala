package models

case class GameRound(matchWeek : Int, var userPicks : List[UserPick])
