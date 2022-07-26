package by.hayel.server.controller;

import by.hayel.server.exception.BlockedUserException;
import by.hayel.server.exception.TokenRefreshException;
import by.hayel.server.model.token.RefreshToken;
import by.hayel.server.model.user.User;
import by.hayel.server.model.user.UserPrincipal;
import by.hayel.server.payload.ServerResponse;
import by.hayel.server.payload.request.SignInRequest;
import by.hayel.server.payload.request.SignOutRequest;
import by.hayel.server.payload.request.SignUpRequest;
import by.hayel.server.payload.request.TokenRefreshRequest;
import by.hayel.server.payload.response.JwtResponse;
import by.hayel.server.payload.response.MessageResponse;
import by.hayel.server.payload.response.TokenRefreshResponse;
import by.hayel.server.service.JwtService;
import by.hayel.server.service.RefreshTokenService;
import by.hayel.server.service.UserService;
import javax.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/authentication")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
  JwtService jwtService;
  UserService userService;
  RefreshTokenService refreshTokenService;
  AuthenticationManager authenticationManager;
  PasswordEncoder encoder;

  @GetMapping
  public ResponseEntity<ServerResponse> testConnection() {
    return ResponseEntity.ok(MessageResponse.SUCCESSFUL_CONNECTION);
  }

  @PostMapping("/sign-in")
  public ResponseEntity<ServerResponse> authenticateUser(
      @Valid @RequestBody SignInRequest request) {
    var authenticationToken =
        new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
    Authentication authentication = authenticationManager.authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
    String token = jwtService.generateToken(principal);
    RefreshToken refreshToken = refreshTokenService.createRefreshToken(principal.getId());
    userService.registerSignIn(principal.getId());
    ServerResponse response =
        new JwtResponse(
            token,
            refreshToken.getToken(),
            principal.getId(),
            principal.getUsername(),
            principal.getEmail());
    return ResponseEntity.ok(response);
  }

  @PostMapping("/sign-up")
  public ResponseEntity<ServerResponse> registerUser(
      @Valid @RequestBody SignUpRequest signUpRequest) {
    if (userService.isUsernameAlreadyExist(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(MessageResponse.USERNAME_ALREADY_EXIST);
    }
    if (userService.isEmailAlreadyExist(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(MessageResponse.EMAIL_ALREADY_EXIST);
    }
    User user = new User();
    user.setUsername(signUpRequest.getUsername());
    user.setEmail(signUpRequest.getEmail());
    user.setPassword(encoder.encode(signUpRequest.getPassword()));
    userService.save(user);
    return ResponseEntity.ok(MessageResponse.USER_REGISTRATION_SUCCESS);
  }

  @PostMapping("/sign-out")
  public ResponseEntity<ServerResponse> logoutUser(
      @Valid @RequestBody SignOutRequest signOutRequest) {
    refreshTokenService.deleteByUserId(signOutRequest.getUserId());
    userService.registerSignOut(signOutRequest.getUserId());
    return ResponseEntity.ok(MessageResponse.USER_LOGOUT_SUCCESS);
  }

  @PostMapping("/refresh-token")
  public ResponseEntity<ServerResponse> refreshToken(
      @Valid @RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();
    return refreshTokenService
        .findByToken(requestRefreshToken)
        .map(refreshTokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(
            user -> {
              String token = jwtService.generateTokenFromUsername(user.getUsername());
              if(userService.isBlocked(user.getUsername())) throw new BlockedUserException();
              ServerResponse response = new TokenRefreshResponse(token, requestRefreshToken);
              return ResponseEntity.ok(response);
            })
        .orElseThrow(() -> new TokenRefreshException(requestRefreshToken));
  }
}
