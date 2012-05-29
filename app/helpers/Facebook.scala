package helpers

import util.matching.Regex
import play.api.libs.ws.WS
import models.User
import play.api.Logger

object Facebook {
  val app_id = "314167758604221"
  val app_secret = "c3e8ac5be05eb369586f7ed829ef114c"
  //val facebookLoginUriBase = "https://www.facebook.com/dialog/oauth?client_id=" + app_id + "&redirect_uri=http://stark-stream-9606.herokuapp.com/callback&state="
  val AuthenticateBaseUrl = "https://www.facebook.com/dialog/oauth?client_id=" + app_id + "&redirect_uri=http://stark-stream-9606.herokuapp.com:9000/callback&state="
  val AccessTokenBaseUrl = "https://graph.facebook.com/oauth/access_token?client_id=" + app_id + "&redirect_uri=http://stark-stream-9606.herokuapp.com:9000/callback&client_secret=" + app_secret + "&code="
  val UserDetailsBaseUrl = "https://graph.facebook.com/me?access_token="

  def parseAccessTokenResponse(response: String): (String, String) = {
    val r = "access_token=([^&]*)&expires=(.*)".r
    val r(first, second) = response
    (first, second)
  }

  def getUser(code: String): User = {
    val url = AccessTokenBaseUrl + code
    Logger.debug("url = " + url)
    val accessTokenRequest = WS.url(url).get()
    Logger.debug("created url get")
    val response = accessTokenRequest.await(5000).get
    Logger.debug("got response " + response.body)
    val accessToken = parseAccessTokenResponse(response.body)._1
    val userDetailsRequest = WS.url(UserDetailsBaseUrl + accessToken).get()
    val userResponse = userDetailsRequest.await(5000).get
    val user = com.codahale.jerkson.Json.parse[User](userResponse.body)
    return user
  }
}
