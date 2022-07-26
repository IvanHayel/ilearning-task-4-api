package by.hayel.server.payload.response;

import by.hayel.server.payload.ServerResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageResponse implements ServerResponse {
  public static final MessageResponse USERNAME_ALREADY_EXIST =
      new MessageResponse("Username is already taken!");
  public static final MessageResponse EMAIL_ALREADY_EXIST =
      new MessageResponse("Email is already in use!");
  public static final MessageResponse USER_REGISTRATION_SUCCESS =
      new MessageResponse("User registered successfully!");
  public static final MessageResponse USER_LOGOUT_SUCCESS =
      new MessageResponse("You've been signed out!");
  public static final MessageResponse SUCCESSFUL_CONNECTION =
      new MessageResponse("REST API is available!");

  String message;
}
