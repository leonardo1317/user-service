package io.github.leonardofrs.user_service.infrastructure.repository.h2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.leonardofrs.user_service.domain.model.UserSession;
import io.github.leonardofrs.user_service.infrastructure.mapper.UserSessionEntityToUserSessionMapper;
import io.github.leonardofrs.user_service.infrastructure.mapper.UserSessionToUserSessionEntityMapper;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.UserEntity;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.UserSessionEntity;
import jakarta.persistence.EntityManager;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultCreateUserSessionRepositoryTest {

  private EntityManager entityManager;
  private UserSessionToUserSessionEntityMapper toEntityMapper;
  private UserSessionEntityToUserSessionMapper toDomainMapper;
  private UserSessionRepository userSessionRepository;
  private DefaultCreateUserSessionRepository repository;

  @BeforeEach
  void setUp() {
    entityManager = mock(EntityManager.class);
    toEntityMapper = mock(UserSessionToUserSessionEntityMapper.class);
    toDomainMapper = mock(UserSessionEntityToUserSessionMapper.class);
    userSessionRepository = mock(UserSessionRepository.class);

    repository = new DefaultCreateUserSessionRepository(
        entityManager,
        toEntityMapper,
        toDomainMapper,
        userSessionRepository
    );
  }

  @Test
  @DisplayName("should create a new user session and map all entities correctly")
  void shouldCreateUserSessionSuccessfully() {
    UUID userId = UUID.randomUUID();
    UserSession userSession = new UserSession(UUID.randomUUID(), "jwt-token", null, null, userId);

    UserEntity userEntity = new UserEntity(
        userId, "test", "test@example.com", "secret", null, null, null, true
    );

    UserSessionEntity mappedEntity = new UserSessionEntity(UUID.randomUUID(), "jwt-token", null,
        null);
    UserSessionEntity savedEntity = new UserSessionEntity(UUID.randomUUID(), "jwt-token", null,
        null);
    UserSession mappedBackToDomain = new UserSession(UUID.randomUUID(), "jwt-token", null, null,
        userId);

    when(entityManager.getReference(UserEntity.class, userId)).thenReturn(userEntity);
    when(toEntityMapper.map(userSession)).thenReturn(mappedEntity);
    when(userSessionRepository.save(mappedEntity)).thenReturn(savedEntity);
    when(toDomainMapper.map(savedEntity)).thenReturn(mappedBackToDomain);

    UserSession result = repository.execute(userSession);

    assertThat(result).isEqualTo(mappedBackToDomain);
    assertThat(mappedEntity.getUser()).isSameAs(userEntity);

    verify(entityManager).getReference(UserEntity.class, userId);
    verify(toEntityMapper).map(userSession);
    verify(userSessionRepository).save(mappedEntity);
    verify(toDomainMapper).map(savedEntity);
  }

  @Test
  @DisplayName("should set user entity reference before saving session")
  void shouldSetUserBeforeSaving() {
    UUID userId = UUID.randomUUID();
    UserSession userSession = new UserSession(UUID.randomUUID(), "jwt", null, null, userId);

    UserEntity userEntity = new UserEntity(
        userId, "test", "test@example.com", "pwd", null, null, null, true
    );

    UserSessionEntity mappedEntity = new UserSessionEntity(UUID.randomUUID(), "jwt", null, null);

    when(entityManager.getReference(UserEntity.class, userId)).thenReturn(userEntity);
    when(toEntityMapper.map(userSession)).thenReturn(mappedEntity);
    when(userSessionRepository.save(mappedEntity)).thenReturn(mappedEntity);
    when(toDomainMapper.map(mappedEntity)).thenReturn(userSession);

    repository.execute(userSession);

    assertThat(mappedEntity.getUser()).isSameAs(userEntity);
    verify(userSessionRepository).save(mappedEntity);
  }

  @Test
  @DisplayName("should return mapped domain session after saving entity")
  void shouldReturnMappedSessionAfterSave() {
    UUID userId = UUID.randomUUID();
    UserSession domainSession = new UserSession(UUID.randomUUID(), "jwt", null, null, userId);

    UserEntity userEntity = new UserEntity(
        userId, "test", "test@example.com", "pass", null, null, null, true
    );

    UserSessionEntity entityToSave = new UserSessionEntity(UUID.randomUUID(), "jwt", null, null);
    UserSessionEntity savedEntity = new UserSessionEntity(UUID.randomUUID(), "jwt", null, null);
    UserSession expectedSession = new UserSession(UUID.randomUUID(), "jwt", null, null, userId);

    when(entityManager.getReference(UserEntity.class, userId)).thenReturn(userEntity);
    when(toEntityMapper.map(domainSession)).thenReturn(entityToSave);
    when(userSessionRepository.save(entityToSave)).thenReturn(savedEntity);
    when(toDomainMapper.map(savedEntity)).thenReturn(expectedSession);

    UserSession result = repository.execute(domainSession);

    assertThat(result).isEqualTo(expectedSession);
  }
}