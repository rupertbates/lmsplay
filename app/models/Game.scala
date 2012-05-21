package models

import java.util.Date
import org.ektorp.support._

case class Game(kickOffTime : String, homeTeam : Team,  awayTeam : Team) extends CouchDbDocument {
  @TypeDiscriminator
  val documentType = "game"
}
