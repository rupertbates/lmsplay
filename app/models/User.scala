package models

case class User(id: String, name : String, gender : String, email: String, password: String){
}
object User {
  /**
   * Authenticate a User.
   */
  def authenticate(email: String, password: String): Boolean = {
    email == password
  }

}
