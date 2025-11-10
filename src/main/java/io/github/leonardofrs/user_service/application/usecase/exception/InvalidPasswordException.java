package io.github.leonardofrs.user_service.application.usecase.exception;

public class InvalidPasswordException extends RuntimeException {

  public InvalidPasswordException(String message) {
    super(message);
  }
}
