package models
import org.ektorp.support._

case class Team(name : String, score : Int) extends CouchDbDocument {
  @TypeDiscriminator
  val documentType = "team"
}
