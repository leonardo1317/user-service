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
    requireNonNull(password, "password no puede ser null");
    String passwordRegex = regexRepository.execute();
    if (isNull(passwordRegex)) {
      throw new ConfigurationException("La expresión regular del password no fue provista");
    }

    if (!password.matches(passwordRegex)) {
      throw new InvalidPasswordException(
          "El password no cumple con la política de seguridad requerida"
      );
    }
  }
}
