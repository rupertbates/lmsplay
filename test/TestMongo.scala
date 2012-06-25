import com.codahale.jerkson.Json
import com.mongodb.casbah.commons.MongoDBObject
import models.Game
import org.specs2.mutable.Specification
import services.GameService

class TestMongo extends Specification {
  "GameRepository" should {
    "be able to find a game by id" in {
//      val game = Json.parse[Game]("{\"name\":\"test\",\"order\":1,\"players\":[{\"username\":\"rupert.bates\"},{\"username\":\"gibbons\"},{\"username\":\"gibbons\"}],\"id\":\"4fd8a5220c6b68fc4d1468cb\",\"created\":1339598114566,\"creator\":\"rupert.bates\"}")
//      GameService.GameRepository.save(game)
      //val fetched = GameService.GameRepository.findOneById("4e65e0200c6baa7685237563")
      //fetched mustNotEqual None

      val fetched2 = GameService.GameRepository.findOne(MongoDBObject("_id" -> "4e65e0200c6baa7685237563")) // GameService.getGameContainingUser("4e65e0200c6baa7685237563", "rupert.bates")

      fetched2 mustNotEqual None
    }


  }
}
