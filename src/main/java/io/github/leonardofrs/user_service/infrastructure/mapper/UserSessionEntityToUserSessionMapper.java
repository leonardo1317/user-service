package io.github.leonardofrs.user_service.infrastructure.mapper;

import static java.util.Objects.requireNonNull;

import io.github.leonardofrs.user_service.domain.model.UserSession;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.UserEntity;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.UserSessionEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UserSessionEntityToUserSessionMapper implements
    Mapper<UserSessionEntity, UserSession> {

  @Override
  public UserSession map(UserSessionEntity source) {
    requireNonNull(source, "source must not be null");
    UUID userId = Optional.ofNullable(source.getUser())
        .map(UserEntity::getId)
        .orElse(null);
    return UserSession.create(
        source.getId(),
        source.getToken(),
        source.getCreatedAt(),
        source.getExpiresAt(),
        userId);
  }
}
