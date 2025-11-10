package io.github.leonardofrs.user_service.infrastructure.mapper;

import io.github.leonardofrs.user_service.domain.model.UserSession;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.UserSessionEntity;
import org.springframework.stereotype.Component;

@Component
public class UserSessionToUserSessionEntityMapper implements
    Mapper<UserSession, UserSessionEntity> {

  @Override
  public UserSessionEntity map(UserSession source) {
    return new UserSessionEntity(
        source.id(),
        source.token(),
        source.createdAt(),
        source.expiresAt()
    );
  }
}
