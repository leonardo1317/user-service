package io.github.leonardofrs.user_service.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserSession(
    UUID id,
    String token,
    LocalDateTime createdAt,
    LocalDateTime expiresAt,
    UUID userId
) {

  public static UserSession create(
      String token,
      LocalDateTime createdAt,
      LocalDateTime expiresAt,
      UUID userId
  ) {
    return create(
        UUID.randomUUID(),
        token,
        createdAt,
        expiresAt,
        userId
    );
  }

  public static UserSession create(
      UUID id,
      String token,
      LocalDateTime createdAt,
      LocalDateTime expiresAt,
      UUID userId
  ) {
    return new UserSession(
        id,
        token,
        createdAt,
        expiresAt,
        userId
    );
  }
}
