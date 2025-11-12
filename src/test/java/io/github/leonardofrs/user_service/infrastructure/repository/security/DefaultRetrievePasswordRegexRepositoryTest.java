package io.github.leonardofrs.user_service.infrastructure.repository.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultRetrievePasswordRegexRepositoryTest {

  @Test
  @DisplayName("should return the configured password regex when execute is called")
  void shouldReturnConfiguredPasswordRegexWhenExecuteIsCalled() {
    String expectedRegex = "^(?=.*[A-Z])(?=.*[0-9]).{8,}$";
    DefaultRetrievePasswordRegexRepository repository =
        new DefaultRetrievePasswordRegexRepository(expectedRegex);

    String actualRegex = repository.execute();

    assertEquals(expectedRegex, actualRegex);
  }

  @Test
  @DisplayName("should return the same regex provided in constructor")
  void shouldReturnSameRegexProvidedInConstructor() {
    String regex = "[A-Za-z0-9]{10,}";
    DefaultRetrievePasswordRegexRepository repository =
        new DefaultRetrievePasswordRegexRepository(regex);

    String result = repository.execute();

    assertEquals(regex, result);
  }
}