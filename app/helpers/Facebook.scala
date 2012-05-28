package helpers

import util.matching.Regex

object Facebook {
  val app_id = "314167758604221"
  val app_secret = "c3e8ac5be05eb369586f7ed829ef114c"
  //val facebookLoginUriBase = "https://www.facebook.com/dialog/oauth?client_id=" + app_id + "&redirect_uri=http://stark-stream-9606.herokuapp.com/callback&state="
  val AuthenticateBaseUrl = "https://www.facebook.com/dialog/oauth?client_id=" + app_id + "&redirect_uri=http://stark-stream-9606.herokuapp.com:9000/callback&state="
  val AccessTokenBaseUrl = "https://graph.facebook.com/oauth/access_token?client_id=" + app_id + "&redirect_uri=http://stark-stream-9606.herokuapp.com:9000/callback&client_secret=" + app_secret + "&code="
  val UserDetailsBaseUrl = "https://graph.facebook.com/me?access_token="
  def parseAccessTokenResponse(response : String) : (String, String) = {

    val matches = new Regex("access_token=([^&]*)&expires=(.*)").findAllIn(response)
    val result = (matches.group(1), matches.group(2))
    result
  }
}
