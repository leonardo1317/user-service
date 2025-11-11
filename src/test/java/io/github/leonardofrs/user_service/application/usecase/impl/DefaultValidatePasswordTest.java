package io.github.leonardofrs.user_service.application.usecase.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.leonardofrs.user_service.application.usecase.exception.ConfigurationException;
import io.github.leonardofrs.user_service.application.usecase.exception.InvalidPasswordException;
import io.github.leonardofrs.user_service.domain.repository.RetrievePasswordRegexRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultValidatePasswordTest {

  private RetrievePasswordRegexRepository regexRepository;
  private DefaultValidatePassword validator;

  @BeforeEach
  void setUp() {
    regexRepository = mock(RetrievePasswordRegexRepository.class);
    validator = new DefaultValidatePassword(regexRepository);
  }

  @Test
  @DisplayName("should validate password successfully when it matches regex")
  void shouldValidatePasswordSuccessfully() {
    String validPassword = "strongPass1!";
    String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$";
    when(regexRepository.execute()).thenReturn(regex);

    validator.execute(validPassword);

    verify(regexRepository).execute();
  }

  @Test
  @DisplayName("should throw ConfigurationException when regex is null")
  void shouldThrowConfigurationExceptionWhenRegexIsNull() {
    when(regexRepository.execute()).thenReturn(null);

    assertThatThrownBy(() -> validator.execute("anyPassword1!"))
        .isInstanceOf(ConfigurationException.class)
        .hasMessage("La expresión regular del password no fue provista");

    verify(regexRepository).execute();
  }

  @Test
  @DisplayName("should throw InvalidPasswordException when password does not match regex")
  void shouldThrowInvalidPasswordExceptionWhenPasswordIsInvalid() {
    String invalidPassword = "weak";
    String regex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*]).{8,}$";
    when(regexRepository.execute()).thenReturn(regex);

    assertThatThrownBy(() -> validator.execute(invalidPassword))
        .isInstanceOf(InvalidPasswordException.class)
        .hasMessage("El password no cumple con la política de seguridad requerida");

    verify(regexRepository).execute();
  }

  @Test
  @DisplayName("should throw NullPointerException when password is null")
  void shouldThrowNullPointerExceptionWhenPasswordIsNull() {
    when(regexRepository.execute()).thenReturn(".*");

    assertThatThrownBy(() -> validator.execute(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("password no puede ser null");
  }
}
