package io.github.leonardofrs.user_service.infrastructure.repository.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

class DefaultEncryptPasswordRepositoryTest {

  private DefaultEncryptPasswordRepository repository;
  private PasswordEncoder passwordEncoder;

  @BeforeEach
  void setUp() {
    passwordEncoder = Mockito.mock(PasswordEncoder.class);
    repository = new DefaultEncryptPasswordRepository(passwordEncoder);
  }

  @Test
  @DisplayName("should encrypt password using provided PasswordEncoder")
  void shouldEncryptPasswordUsingProvidedPasswordEncoder() {
    String rawPassword = "mySecret123";
    String encodedPassword = "$2a$10$encodedValue";
    when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

    String result = repository.execute(rawPassword);

    verify(passwordEncoder, times(1)).encode(rawPassword);
    assertThat(result).isEqualTo(encodedPassword);
  }

  @Test
  @DisplayName("should return non-null encrypted password")
  void shouldReturnNonNullEncryptedPassword() {
    String rawPassword = "test123";
    when(passwordEncoder.encode(rawPassword)).thenReturn("encrypted123");

    String result = repository.execute(rawPassword);

    assertThat(result).isNotNull().isNotBlank();
  }

  @Test
  @DisplayName("should produce different hash for different inputs")
  void shouldProduceDifferentHashForDifferentInputs() {
    when(passwordEncoder.encode("password1")).thenReturn("encoded1");
    when(passwordEncoder.encode("password2")).thenReturn("encoded2");

    String result1 = repository.execute("password1");
    String result2 = repository.execute("password2");

    assertThat(result1).isNotEqualTo(result2);
  }
}
