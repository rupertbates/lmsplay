package TestSerialization

import org.specs2.mutable._
import helpers.Facebook
import models.Game
import com.codahale.jerkson.Json

class SerializeModel extends Specification {
  "Jerkson" should {
    "be able to parse a game" in {
      val game = Json.parse[Game]("{\"name\":\"test\",\"order\":1,\"players\":[{\"username\":\"rupert.bates\"},{\"username\":\"gibbons\"},{\"username\":\"gibbons\"}],\"id\":\"4fd8a5220c6b68fc4d1468cb\",\"created\":1339598114566,\"creator\":\"rupert.bates\"}")
      game != null
    }


  }
}
