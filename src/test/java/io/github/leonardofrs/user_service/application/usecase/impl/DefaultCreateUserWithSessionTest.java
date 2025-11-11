package io.github.leonardofrs.user_service.application.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.leonardofrs.user_service.application.usecase.CreateUser;
import io.github.leonardofrs.user_service.application.usecase.CreateUserSession;
import io.github.leonardofrs.user_service.domain.model.CreatedUser;
import io.github.leonardofrs.user_service.domain.model.Phone;
import io.github.leonardofrs.user_service.domain.model.User;
import io.github.leonardofrs.user_service.domain.model.UserSession;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultCreateUserWithSessionTest {

  private CreateUser createUser;
  private CreateUserSession createUserSession;
  private DefaultCreateUserWithSession createUserWithSession;

  @BeforeEach
  void setUp() {
    createUser = mock(CreateUser.class);
    createUserSession = mock(CreateUserSession.class);
    createUserWithSession = new DefaultCreateUserWithSession(createUser, createUserSession);
  }

  @Test
  @DisplayName("should create a new user with session successfully")
  void shouldCreateUserWithSessionSuccessfully() {
    UUID userId = UUID.randomUUID();
    var user = User.create(
        userId,
        "test",
        "test@example.com",
        "",
        List.of(Phone.create("1234567", "1", "57")),
        LocalDateTime.now(),
        LocalDateTime.now(),
        null,
        true
    );

    User createdUser = User.create(
        user.id(),
        user.name(),
        user.email(),
        user.password(),
        user.phones(),
        user.created(),
        user.modified(),
        LocalDateTime.now(),
        true
    );

    var userSession = UserSession.create("jwt-token-value", LocalDateTime.now(),
        LocalDateTime.now(), userId);

    when(createUser.execute(user)).thenReturn(createdUser);
    when(createUserSession.execute(createdUser)).thenReturn(userSession);

    CreatedUser result = createUserWithSession.execute(user);

    assertThat(result).isNotNull();
    assertThat(result.id()).isEqualTo(user.id());
    assertThat(result.name()).isEqualTo(user.name());
    assertThat(result.email()).isEqualTo(user.email());
    assertThat(result.phones()).isEqualTo(user.phones());
    assertThat(result.token()).isEqualTo("jwt-token-value");
    assertThat(result.isActive()).isTrue();

    verify(createUser).execute(user);
    verify(createUserSession).execute(createdUser);
  }
}