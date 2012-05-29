package models

import org.codehaus.jackson.annotate.JsonIgnore

case class User(id: String, name : String, gender : String, email: String,@JsonIgnore password: String){
}
object User {
  /**
   * Authenticate a User.
   */
  def authenticate(email: String, password: String): Boolean = {
    email == password
  }

}
