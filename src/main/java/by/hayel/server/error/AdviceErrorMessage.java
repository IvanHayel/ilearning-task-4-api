package by.hayel.server.error;

import java.time.ZonedDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdviceErrorMessage {
  ZonedDateTime timestamp;
  int statusCode;
  String message;
  String description;

  public AdviceErrorMessage(int statusCode, String message, String description) {
    this.statusCode = statusCode;
    this.message = message;
    this.description = description;
    timestamp = ZonedDateTime.now();
  }
}
