package components

object ComponentRegistry extends
UserServiceComponent with
UserRepositoryComponent {
  val userRepository = new UserRepository
  val userService = new UserService
}

