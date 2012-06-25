package TestSerialization

import org.specs2.mutable._
import helpers.Facebook
import models.Game
import com.codahale.jerkson.Json

class SerializeModel extends Specification {
  "Jerkson" should {
    "be able to parse a game" in {
      val game = Json.parse[Game]("{ \"id\" : \"4e65e0200c6baa7685237563\", \"name\" : \"My test game\", \"created\" : \"2011-09-06T08:56:00.393Z\", \"creator\" : \"rupert.bates\", \"players\" : [ \"rupert.bates\" ], \"gameRounds\" : [ ] }")
      game != null
    }


  }
}
