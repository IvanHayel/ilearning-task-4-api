package by.hayel.server.controller;

import by.hayel.server.model.user.User;
import by.hayel.server.payload.ServerResponse;
import by.hayel.server.payload.response.MessageResponse;
import by.hayel.server.service.UserService;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
  UserService userService;

  @GetMapping
  public Collection<User> getAllUsers() {
    return userService.getAllUsers();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ServerResponse> deleteById(@PathVariable Long id) {
    userService.deleteById(id);
    return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
  }

  @PutMapping("/block/{id}")
  public ResponseEntity<ServerResponse> blockUser(@PathVariable Long id) {
    userService.blockById(id);
    return ResponseEntity.ok(new MessageResponse("User blocked successfully!"));
  }

  @PutMapping("/unblock/{id}")
  public ResponseEntity<ServerResponse> unblockUser(@PathVariable Long id) {
    userService.unblockById(id);
    return ResponseEntity.ok(new MessageResponse("User unblocked successfully!"));
  }
}
