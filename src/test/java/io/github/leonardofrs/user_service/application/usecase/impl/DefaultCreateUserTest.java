package io.github.leonardofrs.user_service.application.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import io.github.leonardofrs.user_service.application.usecase.ValidateEmail;
import io.github.leonardofrs.user_service.application.usecase.ValidatePassword;
import io.github.leonardofrs.user_service.domain.model.User;
import io.github.leonardofrs.user_service.domain.repository.CreateUserRepository;
import io.github.leonardofrs.user_service.domain.repository.EncryptPasswordRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultCreateUserTest {

  private ValidateEmail validateEmail;
  private ValidatePassword validatePassword;
  private EncryptPasswordRepository encryptPasswordRepository;
  private CreateUserRepository createUserRepository;
  private DefaultCreateUser defaultCreateUser;

  @BeforeEach
  void setUp() {
    validateEmail = mock(ValidateEmail.class);
    validatePassword = mock(ValidatePassword.class);
    encryptPasswordRepository = mock(EncryptPasswordRepository.class);
    createUserRepository = mock(CreateUserRepository.class);

    defaultCreateUser = new DefaultCreateUser(
        validateEmail,
        validatePassword,
        encryptPasswordRepository,
        createUserRepository
    );
  }

  @Test
  @DisplayName("should validate email and password, encrypt password, and create user")
  void shouldValidateEncryptAndCreateUserSuccessfully() {
    UUID userId = UUID.randomUUID();
    var user = new User(
        userId,
        "test",
        "test@example.com",
        "rawPassword123",
        Collections.emptyList(),
        LocalDateTime.now(),
        LocalDateTime.now(),
        null,
        true
    );

    String encryptedPassword = "encryptedPassword123";
    var savedUser = new User(
        userId,
        user.name(),
        user.email(),
        encryptedPassword,
        Collections.emptyList(),
        user.created(),
        user.modified(),
        user.lastLogin(),
        user.isActive()
    );

    when(encryptPasswordRepository.execute("rawPassword123")).thenReturn(encryptedPassword);
    when(createUserRepository.execute(any(User.class))).thenReturn(savedUser);

    User result = defaultCreateUser.execute(user);

    assertThat(result).isEqualTo(savedUser);
    verify(validateEmail).execute(user.email());
    verify(validatePassword).execute(user.password());
    verify(encryptPasswordRepository).execute(user.password());
    verify(createUserRepository).execute(any(User.class));
  }

  @Test
  @DisplayName("should throw exception if email is invalid")
  void shouldThrowExceptionIfEmailInvalid() {
    var user = mock(User.class);
    when(user.email()).thenReturn("invalid-email");
    doThrow(new RuntimeException("Invalid email")).when(validateEmail).execute("invalid-email");

    assertThatThrownBy(() -> defaultCreateUser.execute(user))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Invalid email");

    verify(validateEmail).execute("invalid-email");
    verifyNoInteractions(validatePassword, encryptPasswordRepository, createUserRepository);
  }

  @Test
  @DisplayName("should throw exception if password is invalid")
  void shouldThrowExceptionIfPasswordInvalid() {
    var user = mock(User.class);
    when(user.email()).thenReturn("test@example.com");
    when(user.password()).thenReturn("weakPass");
    doNothing().when(validateEmail).execute("test@example.com");
    doThrow(new RuntimeException("Invalid password")).when(validatePassword).execute("weakPass");

    assertThatThrownBy(() -> defaultCreateUser.execute(user))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Invalid password");

    verify(validateEmail).execute("test@example.com");
    verify(validatePassword).execute("weakPass");
    verifyNoInteractions(encryptPasswordRepository, createUserRepository);
  }
}