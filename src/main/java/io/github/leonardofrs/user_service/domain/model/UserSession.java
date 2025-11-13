package io.github.leonardofrs.user_service.domain.model;

import java.time.Instant;
import java.util.UUID;

public record UserSession(
    UUID id,
    String token,
    Instant createdAt,
    Instant expiresAt,
    UUID userId
) {

  public static UserSession create(
      String token,
      Instant createdAt,
      Instant expiresAt,
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
      Instant createdAt,
      Instant expiresAt,
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
