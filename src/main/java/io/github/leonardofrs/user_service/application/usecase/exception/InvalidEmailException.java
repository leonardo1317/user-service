package io.github.leonardofrs.user_service.application.usecase.exception;

public class InvalidEmailException extends RuntimeException {

  public InvalidEmailException(String message) {
    super(message);
  }
}
