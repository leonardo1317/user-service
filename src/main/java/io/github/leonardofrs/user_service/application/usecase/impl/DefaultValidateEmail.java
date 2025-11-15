package io.github.leonardofrs.user_service.application.usecase.impl;

import static java.util.Objects.requireNonNull;

import io.github.leonardofrs.user_service.application.usecase.ValidateEmail;
import io.github.leonardofrs.user_service.application.usecase.exception.InvalidEmailException;

public class DefaultValidateEmail implements ValidateEmail {

  private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

  @Override
  public void execute(String email) {
    requireNonNull(email, "email must not be null");
    if (!email.matches(EMAIL_REGEX)) {
      throw new InvalidEmailException(
          "The email does not comply with the required security policy"
      );
    }
  }
}
