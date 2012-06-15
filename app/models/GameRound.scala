package models

case class GameRound(competitionWeek : Int, var userPicks : List[UserPick])
