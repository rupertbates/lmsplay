
import play.api._
import repositories.MatchRepository
import com.mongodb.casbah.commons.conversions.scala._

object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.debug("App onStart running")

    RegisterJodaTimeConversionHelpers()

    RegisterConversionHelpers()

    if(MatchRepository.count() == 0){
      Logger.debug("No matches in the db, importing them now")
      ImportMatches.importMatches()
      Logger.debug("Finished importing matches")
    }
  }
}
