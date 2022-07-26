package by.hayel.server.payload.request;

import by.hayel.server.payload.ClientRequest;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignOutRequest implements ClientRequest {
  @Min(0)
  @NotNull
  Long userId;
}
