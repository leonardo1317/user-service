package io.github.leonardofrs.user_service.infrastructure.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.leonardofrs.user_service.domain.model.UserSession;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.UserEntity;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.UserSessionEntity;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserSessionEntityToUserSessionMapperTest {

  private UserSessionEntityToUserSessionMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new UserSessionEntityToUserSessionMapper();
  }

  @Test
  @DisplayName("should map UserSessionEntity to UserSession successfully")
  void shouldMapUserSessionEntityToUserSessionSuccessfully() {
    UUID sessionId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime expiresAt = createdAt.plusSeconds(3600);

    UserSessionEntity entity = new UserSessionEntity(
        sessionId,
        "jwt-token-123",
        createdAt,
        expiresAt);

    UserEntity userEntity = new UserEntity(
        userId,
        "test",
        "test@example.com",
        "",
        LocalDateTime.now(),
        LocalDateTime.now(),
        LocalDateTime.now(),
        true);

    entity.setUser(userEntity);

    UserSession result = mapper.map(entity);

    assertNotNull(result);
    assertEquals(sessionId, result.id());
    assertEquals("jwt-token-123", result.token());
    assertEquals(createdAt, result.createdAt());
    assertEquals(expiresAt, result.expiresAt());
    assertEquals(userId, result.userId());
  }


  @Test
  @DisplayName("should set userId to null when UserEntity in UserSessionEntity is null")
  void shouldSetUserIdToNullWhenUserEntityIsNull() {
    UUID sessionId = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime expiresAt = createdAt.plusSeconds(3600);

    UserSessionEntity entity = new UserSessionEntity(
        sessionId,
        "jwt-token-123",
        createdAt,
        expiresAt
    );

    entity.setUser(null);

    UserSession result = mapper.map(entity);

    assertNotNull(result);
    assertEquals(sessionId, result.id());
    assertEquals("jwt-token-123", result.token());
    assertEquals(createdAt, result.createdAt());
    assertEquals(expiresAt, result.expiresAt());
    assertNull(result.userId(), "userId should be null when user is missing");
  }

  @Test
  @DisplayName("should throw NullPointerException when UserSessionEntity is null")
  void shouldThrowExceptionWhenUserSessionEntityIsNull() {
    assertThrows(NullPointerException.class, () -> mapper.map((UserSessionEntity) null));
  }
}
