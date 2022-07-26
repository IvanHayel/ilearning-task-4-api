package by.hayel.server.controller.advice;

import by.hayel.server.error.AdviceErrorMessage;
import by.hayel.server.exception.BlockedUserException;
import by.hayel.server.exception.TokenRefreshException;
import by.hayel.server.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class TokenControllerAdvice {
  @ExceptionHandler(TokenRefreshException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public AdviceErrorMessage handleTokenRefreshException(
      TokenRefreshException exception, WebRequest request) {
    return new AdviceErrorMessage(
        HttpStatus.FORBIDDEN.value(), exception.getMessage(), request.getDescription(false));
  }

  @ExceptionHandler(UserNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public AdviceErrorMessage handleUserNotFoundException(
      UserNotFoundException exception, WebRequest request) {
    return new AdviceErrorMessage(
        HttpStatus.BAD_REQUEST.value(), exception.getMessage(), request.getDescription(false));
  }

  @ExceptionHandler(BlockedUserException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public AdviceErrorMessage handleBlockedUserException(
      BlockedUserException exception, WebRequest request) {
    return new AdviceErrorMessage(
        HttpStatus.FORBIDDEN.value(), exception.getMessage(), request.getDescription(false));
  }
}
