package io.github.leonardofrs.user_service.application.usecase.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.leonardofrs.user_service.application.usecase.exception.InvalidEmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultValidateEmailTest {

  private DefaultValidateEmail validator;

  @BeforeEach
  void setUp() {
    validator = new DefaultValidateEmail();
  }

  @Test
  @DisplayName("should validate email successfully when it matches regex")
  void shouldValidateEmailSuccessfully() {
    String validEmail = "test@example.com";

    validator.execute(validEmail);
  }

  @Test
  @DisplayName("should throw InvalidEmailException when email does not match regex")
  void shouldThrowInvalidEmailExceptionWhenEmailIsInvalid() {
    String invalidEmail = "test@";

    assertThatThrownBy(() -> validator.execute(invalidEmail))
        .isInstanceOf(InvalidEmailException.class)
        .hasMessage("The email does not comply with the required security policy");
  }

  @Test
  @DisplayName("should throw NullPointerException when email is null")
  void shouldThrowNullPointerExceptionWhenEmailIsNull() {
    String nullEmail = null;

    assertThatThrownBy(() -> validator.execute(nullEmail))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("email must not be null");
  }
}
