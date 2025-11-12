package io.github.leonardofrs.user_service.infrastructure.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.leonardofrs.user_service.domain.model.UserSession;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.UserSessionEntity;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserSessionToUserSessionEntityMapperTest {

  private UserSessionToUserSessionEntityMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new UserSessionToUserSessionEntityMapper();
  }

  @Test
  @DisplayName("should map UserSession to UserSessionEntity successfully")
  void shouldMapUserSessionToUserSessionEntitySuccessfully() {
    UUID sessionId = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime expiresAt = createdAt.plusHours(1);

    UserSession session = UserSession.create(sessionId, "jwt-token-xyz", createdAt, expiresAt,
        UUID.randomUUID());

    UserSessionEntity result = mapper.map(session);

    assertNotNull(result);
    assertEquals(sessionId, result.getId());
    assertEquals("jwt-token-xyz", result.getToken());
    assertEquals(createdAt, result.getCreatedAt());
    assertEquals(expiresAt, result.getExpiresAt());
  }

  @Test
  @DisplayName("should throw NullPointerException when UserSession is null")
  void shouldThrowExceptionWhenUserSessionIsNull() {
    assertThrows(NullPointerException.class, () -> mapper.map((UserSession) null));
  }
}
