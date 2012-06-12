
import play.api._

object Global extends GlobalSettings {

  override def onStart(app: Application) {

    import com.mongodb.casbah.commons.conversions.scala._

    RegisterJodaTimeConversionHelpers()

    RegisterConversionHelpers()
  }
}
