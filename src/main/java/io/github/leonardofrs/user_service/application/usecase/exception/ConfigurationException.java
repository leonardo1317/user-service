package io.github.leonardofrs.user_service.application.usecase.exception;

public class ConfigurationException extends RuntimeException {

  public ConfigurationException(String message) {
    super(message);
  }
}
