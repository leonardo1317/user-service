package io.github.leonardofrs.user_service.infrastructure.repository.h2;

import io.github.leonardofrs.user_service.domain.model.UserSession;
import io.github.leonardofrs.user_service.domain.repository.CreateUserSessionRepository;
import io.github.leonardofrs.user_service.infrastructure.mapper.UserSessionEntityToUserSessionMapper;
import io.github.leonardofrs.user_service.infrastructure.mapper.UserSessionToUserSessionEntityMapper;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.UserEntity;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.UserSessionEntity;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Component;

@Component
public class DefaultCreateUserSessionRepository implements CreateUserSessionRepository {

  private final EntityManager entityManager;
  private final UserSessionToUserSessionEntityMapper userSessionToUserSessionEntityMapper;
  private final UserSessionEntityToUserSessionMapper userSessionEntityToUserSessionMapper;
  private final UserSessionRepository userSessionRepository;

  public DefaultCreateUserSessionRepository(
      EntityManager entityManager,
      UserSessionToUserSessionEntityMapper userSessionToUserSessionEntityMapper,
      UserSessionEntityToUserSessionMapper userSessionEntityToUserSessionMapper,
      UserSessionRepository userSessionRepository) {
    this.entityManager = entityManager;
    this.userSessionToUserSessionEntityMapper = userSessionToUserSessionEntityMapper;
    this.userSessionEntityToUserSessionMapper = userSessionEntityToUserSessionMapper;
    this.userSessionRepository = userSessionRepository;
  }

  @Override
  public UserSession execute(UserSession userSession) {
    UserEntity userEntity = entityManager.getReference(UserEntity.class, userSession.userId());
    UserSessionEntity userSessionEntity = userSessionToUserSessionEntityMapper.map(userSession);
    userSessionEntity.setUser(userEntity);
    return userSessionEntityToUserSessionMapper.map(userSessionRepository.save(userSessionEntity));
  }
}
