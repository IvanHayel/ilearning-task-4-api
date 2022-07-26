package by.hayel.server.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serial;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserPrincipal implements UserDetails {
  @Serial private static final long serialVersionUID = 1L;

  Long id;
  String username;
  @JsonIgnore String password;
  String email;
  ZonedDateTime createdAt;
  ZonedDateTime updatedAt;
  ZonedDateTime lastLogin;
  boolean isActive;
  boolean isBlocked;

  public static UserPrincipal build(User user) {
    return new UserPrincipal(
        user.getId(),
        user.getUsername(),
        user.getPassword(),
        user.getEmail(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getLastLogin(),
        user.isActive(),
        user.isBlocked());
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !isBlocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return !isBlocked;
  }
}
