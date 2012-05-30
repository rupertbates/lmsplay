import helpers.Facebook
import models.User
import org.specs2.mutable._

import com.codahale.jerkson.Json.parse

class HelloWorldSpec extends Specification {

  "The 'Hello world' string" should {
    "contain 11 characters" in {
      "Hello world" must have size (11)
    }
    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
  }
}

class FacebookSpec extends Specification {
  "The Facebook login flow" should {
//    "be able to retrieve an access token" in {
//      Facebook.getUser("AQAkHy3BrTYj77JxSVS8o417mWgycRzK0AvzOuk3WEHft7k7eAwH_0f-szp5fvOm1aBQdeecXSTghRTK5a-RdEWwcEoeoUGLG9sozOeMomQeWuQP_Ms842-JqKiKxs1E1Sq8tglGr6dy-IyLvpO-IWIFH0iBwZdTS9r9QozjLy0JBuzjIVJnM_trYXl5v85vYWM#_=_").name == ""
//    }
    "be able to deserialise a user from a facebook response" in {
      val json = "{\"id\":\"563549923\",\"name\":\"Rupert Bates\",\"first_name\":\"Rupert\",\"last_name\":\"Bates\",\"link\":\"http:\\/\\/www.facebook.com\\/rupert.bates\",\"username\":\"rupert.bates\",\"work\":[{\"employer\":{\"id\":\"107659882599029\",\"name\":\"Guardian News and Media\"},\"location\":{\"id\":\"106078429431815\",\"name\":\"London, United Kingdom\"},\"position\":{\"id\":\"146255685390317\",\"name\":\"Android Developer\"},\"start_date\":\"0000-00\"},{\"employer\":{\"id\":\"109946815694186\",\"name\":\"Opportunity Links\"}}],\"gender\":\"male\",\"email\":\"elias_bland\\u0040yahoo.com\",\"timezone\":1,\"locale\":\"en_US\",\"verified\":true,\"updated_time\":\"2012-03-23T11:23:46+0000\"}"
      parse[User](json) != null
    }
  }
}
