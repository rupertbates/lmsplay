package models

case class User(email: String, name: String, password: String){
}
object User {
  /**
   * Authenticate a User.
   */
  def authenticate(email: String, password: String): Boolean = {
    email == password
  }

}
