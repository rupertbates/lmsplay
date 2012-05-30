package components
import repositories.UserRepository
import com.mongodb.casbah.MongoConnection
import com.novus.salat.dao.SalatDAO
import com.novus.salat.global._
import models.User

trait UserRepositoryComponent {
  val userRepository : UserRepository
  class UserRepository extends SalatDAO[User, Int](collection = MongoConnection()("test")("user")) {

  }
}
