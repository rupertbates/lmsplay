import helpers.Facebook
import models.User
import org.specs2.mutable._

import com.codahale.jerkson.Json.parse

//class HelloWorldSpec extends Specification {
//
//  "The 'Hello world' string" should {
//    "contain 11 characters" in {
//      "Hello world" must have size (11)
//    }
//    "start with 'Hello'" in {
//      "Hello world" must startWith("Hello")
//    }
//    "end with 'world'" in {
//      "Hello world" must endWith("world")
//    }
//  }
//}

class FacebookSpec extends Specification {
  "The Facebook login flow" should {
    "be able to parse an access token" in {
      Facebook.parseAccessTokenResponse("access_token=AAAEduZBGmx70BAFZAXtZB43ynguXkx4gFSZCJyEj6yrQJPSk6ITZAP3iqqSRikZBZAQ9qlckZAQZBO560HfEEvuq8w7STXXKjYyiZBi44QSLPn9QZDZD&expires=5183942")._2 == "5183942"
    }
    "be able to deserialise a user from a facebook response" in {
      val json = "{\"id\":\"563549923\",\"name\":\"Rupert Bates\",\"first_name\":\"Rupert\",\"last_name\":\"Bates\",\"link\":\"http:\\/\\/www.facebook.com\\/rupert.bates\",\"username\":\"rupert.bates\",\"work\":[{\"employer\":{\"id\":\"107659882599029\",\"name\":\"Guardian News and Media\"},\"location\":{\"id\":\"106078429431815\",\"name\":\"London, United Kingdom\"},\"position\":{\"id\":\"146255685390317\",\"name\":\"Android Developer\"},\"start_date\":\"0000-00\"},{\"employer\":{\"id\":\"109946815694186\",\"name\":\"Opportunity Links\"}}],\"gender\":\"male\",\"email\":\"elias_bland\\u0040yahoo.com\",\"timezone\":1,\"locale\":\"en_US\",\"verified\":true,\"updated_time\":\"2012-03-23T11:23:46+0000\"}"
      val user = parse[User](json)
      user != null
    }
  }
}
