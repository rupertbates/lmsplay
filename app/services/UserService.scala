package services

import models.User
import repositories.UserRepository
import helpers.Facebook
import com.mongodb.casbah.MongoConnection
import com.novus.salat.dao.SalatDAO
import com.novus.salat.global._
import com.novus.salat._
import com.mongodb.casbah.Imports._


object UserService {

  def authenticate(code : String): Option[User] = {
    val user = Facebook.getUser(code)
    user.map(saveUser(_))
    user
  }

  def getUserData(user: User): Option[User] = {
    UserRepository.findOneByID(user.id)
  }

  def saveUser(user: User) {
    getUserData(user).getOrElse(UserRepository.save(user))
  }
}

