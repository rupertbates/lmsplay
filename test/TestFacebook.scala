import helpers.Facebook
import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

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
  "Facebook.getUser" should {
    "be able to retrieve an access token" in {
      Facebook.getUser("AQAkHy3BrTYj77JxSVS8o417mWgycRzK0AvzOuk3WEHft7k7eAwH_0f-szp5fvOm1aBQdeecXSTghRTK5a-RdEWwcEoeoUGLG9sozOeMomQeWuQP_Ms842-JqKiKxs1E1Sq8tglGr6dy-IyLvpO-IWIFH0iBwZdTS9r9QozjLy0JBuzjIVJnM_trYXl5v85vYWM#_=_").name == ""
    }
  }
}
