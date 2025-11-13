package io.github.leonardofrs.user_service.application.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.leonardofrs.user_service.domain.dto.Token;
import io.github.leonardofrs.user_service.domain.model.User;
import io.github.leonardofrs.user_service.domain.model.UserSession;
import io.github.leonardofrs.user_service.domain.repository.CreateUserSessionRepository;
import io.github.leonardofrs.user_service.domain.repository.RetrieveTokenRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultCreateUserSessionTest {

  private RetrieveTokenRepository retrieveTokenRepository;
  private CreateUserSessionRepository createUserSessionRepository;
  private DefaultCreateUserSession createUserSession;

  @BeforeEach
  void setUp() {
    retrieveTokenRepository = mock(RetrieveTokenRepository.class);
    createUserSessionRepository = mock(CreateUserSessionRepository.class);
    createUserSession = new DefaultCreateUserSession(retrieveTokenRepository,
        createUserSessionRepository);
  }

  @Test
  @DisplayName("should generate a token and create a user session successfully")
  void shouldGenerateTokenAndCreateUserSessionSuccessfully() {
    UUID userId = UUID.randomUUID();
    var user = User.create(
        userId,
        "test",
        "test@example.com",
        null,
        Collections.emptyList(),
        Instant.now(),
        Instant.now(),
        null,
        true
    );
    Instant createAt = Instant.now();
    Instant expiry = Instant.now().plus(1, ChronoUnit.HOURS);
    Token token = new Token("jwt-token", createAt, expiry);

    UserSession sessionToSave = UserSession.create(token.value(), createAt, expiry, userId);
    UserSession savedSession = UserSession.create(sessionToSave.id(), token.value(), sessionToSave.createdAt(),
        sessionToSave.expiresAt(), userId);

    when(retrieveTokenRepository.execute(user.id().toString(), user.email())).thenReturn(token);
    when(createUserSessionRepository.execute(any(UserSession.class))).thenReturn(savedSession);

    UserSession result = createUserSession.execute(user);

    assertThat(result).isNotNull();
    assertThat(result.token()).isEqualTo(token.value());
    assertThat(result.userId()).isEqualTo(userId);

    verify(retrieveTokenRepository).execute(user.id().toString(), user.email());
    verify(createUserSessionRepository).execute(any(UserSession.class));
  }

  @Test
  @DisplayName("should call token repository with correct user data")
  void shouldCallTokenRepositoryWithCorrectUserData() {
    UUID userId = UUID.randomUUID();
    User user = new User(userId, "test", "test@example.com", null, Collections.emptyList(),
        Instant.now(), null, null, true);

    Instant createAt = Instant.now();
    Instant expiry = Instant.now().plus(1, ChronoUnit.HOURS);
    Token token = new Token("jwt-token", createAt, expiry);
    when(retrieveTokenRepository.execute(user.id().toString(), user.email())).thenReturn(token);
    when(createUserSessionRepository.execute(any(UserSession.class))).thenAnswer(
        invocation -> invocation.getArgument(0));

    createUserSession.execute(user);

    verify(retrieveTokenRepository).execute(user.id().toString(), user.email());
    verify(createUserSessionRepository).execute(any(UserSession.class));
  }
}