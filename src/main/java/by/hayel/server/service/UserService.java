package by.hayel.server.service;

import by.hayel.server.model.user.User;
import java.util.Collection;

public interface UserService {
  Collection<User> getAllUsers();

  User getUserById(Long id);

  boolean isUsernameAlreadyExist(String username);

  boolean isEmailAlreadyExist(String email);

  boolean isBlocked(String username);

  void save(User user);

  void deleteById(Long id);

  void registerSignIn(Long id);

  void registerSignOut(Long id);

  void blockById(Long id);

  void unblockById(Long id);
}
