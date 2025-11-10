package io.github.leonardofrs.user_service.infrastructure.mapper;

import io.github.leonardofrs.user_service.domain.model.UserSession;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.UserSessionEntity;
import org.springframework.stereotype.Component;

@Component
public class UserSessionEntityToUserSessionMapper implements
    Mapper<UserSessionEntity, UserSession> {

  @Override
  public UserSession map(UserSessionEntity source) {
    return UserSession.create(
        source.getId(),
        source.getToken(),
        source.getCreatedAt(),
        source.getExpiresAt(),
        source.getUser().getId());
  }
}
