package by.hayel.server.service.impl;

import by.hayel.server.exception.UserNotFoundException;
import by.hayel.server.model.user.User;
import by.hayel.server.repository.UserRepository;
import by.hayel.server.service.RefreshTokenService;
import by.hayel.server.service.UserService;
import java.time.ZonedDateTime;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
  UserRepository repository;
  RefreshTokenService refreshTokenService;

  @Override
  public Collection<User> getAllUsers() {
    return repository.findAll();
  }

  @Override
  public User getUserById(Long id) {
    return repository.findById(id).orElseThrow(UserNotFoundException::new);
  }

  @Override
  public boolean isUsernameAlreadyExist(String username) {
    return repository.existsByUsername(username);
  }

  @Override
  public boolean isEmailAlreadyExist(String email) {
    return repository.existsByEmail(email);
  }

  @Override
  public boolean isBlocked(String username) {
    return repository.findByUsername(username).map(User::isBlocked).orElse(false);
  }

  @Override
  public void save(User user) {
    repository.save(user);
  }

  @Override
  public void deleteById(Long id) {
    refreshTokenService.deleteByUserId(id);
    repository.deleteById(id);
  }

  @Override
  public void registerSignIn(Long id) {
    User user = repository.findById(id).orElseThrow(UserNotFoundException::new);
    user.setLastLogin(ZonedDateTime.now());
    user.setActive(true);
    repository.save(user);
  }

  @Override
  public void registerSignOut(Long id) {
    User user = repository.findById(id).orElseThrow(UserNotFoundException::new);
    user.setActive(false);
    repository.save(user);
  }

  @Override
  public void blockById(Long id) {
    User user = repository.findById(id).orElseThrow(UserNotFoundException::new);
    user.setBlocked(true);
    user.setActive(false);
    repository.save(user);
  }

  @Override
  public void unblockById(Long id) {
    User user = repository.findById(id).orElseThrow(UserNotFoundException::new);
    user.setBlocked(false);
    repository.save(user);
  }
}
