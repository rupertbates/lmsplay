package components

import models.User
import com.novus.salat.dao.SalatDAO
import com.novus.salat.global._

trait UserServiceComponent {
  this: UserRepositoryComponent =>

  val userService: UserService

  class UserService {

    def authenticate(email : String, password : String) : Boolean = {
      true;
    }

    def getUserData(user: User): Option[User] = {
      //val storedUser = userRepository.findOne(user)
      //storedUser
      Option(user)
    }
    
    def saveUser(user: User){
      getUserData(user).getOrElse(userRepository.save(user))

    }
  }

}
