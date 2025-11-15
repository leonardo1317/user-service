package io.github.leonardofrs.user_service.application.usecase.impl;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

import io.github.leonardofrs.user_service.application.usecase.ValidatePassword;
import io.github.leonardofrs.user_service.application.usecase.exception.ConfigurationException;
import io.github.leonardofrs.user_service.application.usecase.exception.InvalidPasswordException;
import io.github.leonardofrs.user_service.domain.repository.RetrievePasswordRegexRepository;

public class DefaultValidatePassword implements ValidatePassword {

  private final RetrievePasswordRegexRepository regexRepository;

  public DefaultValidatePassword(RetrievePasswordRegexRepository regexRepository) {
    this.regexRepository = regexRepository;
  }

  @Override
  public void execute(String password) {
    requireNonNull(password, "password must not be null");
    String passwordRegex = regexRepository.execute();
    if (isNull(passwordRegex)) {
      throw new ConfigurationException("The password regular expression was not provided");
    }

    if (!password.matches(passwordRegex)) {
      throw new InvalidPasswordException(
          "The password does not meet the required security policy"
      );
    }
  }
}
