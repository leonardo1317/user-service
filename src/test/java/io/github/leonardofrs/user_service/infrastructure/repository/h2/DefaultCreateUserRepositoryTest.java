package io.github.leonardofrs.user_service.infrastructure.repository.h2;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.leonardofrs.user_service.domain.model.User;
import io.github.leonardofrs.user_service.infrastructure.mapper.UserEntityToUserMapper;
import io.github.leonardofrs.user_service.infrastructure.mapper.UserToUserEntityMapper;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.UserEntity;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DefaultCreateUserRepositoryTest {

  private UserToUserEntityMapper toEntityMapper;
  private UserEntityToUserMapper toDomainMapper;
  private UserRepository userRepository;
  private DefaultCreateUserRepository repository;

  @BeforeEach
  void setUp() {
    toEntityMapper = mock(UserToUserEntityMapper.class);
    toDomainMapper = mock(UserEntityToUserMapper.class);
    userRepository = mock(UserRepository.class);

    repository = new DefaultCreateUserRepository(
        toEntityMapper,
        toDomainMapper,
        userRepository
    );
  }

  @Test
  @DisplayName("should create a new user successfully and map entities correctly")
  void shouldCreateUserSuccessfully() {
    UUID userId = UUID.randomUUID();
    User user = new User(userId, "test", "test@example.com", "pwd123", null, null, null, null,
        true);

    UserEntity mappedEntity = new UserEntity(
        userId, "test", "test@example.com", "pwd123", null, null, null, true
    );

    UserEntity savedEntity = new UserEntity(
        userId, "test", "test@example.com", "pwd123", null, null, null, true
    );

    User expectedUser = new User(userId, "test", "test@example.com", "pwd123", null, null, null,
        null, true);

    when(toEntityMapper.map(user)).thenReturn(mappedEntity);
    when(userRepository.save(mappedEntity)).thenReturn(savedEntity);
    when(toDomainMapper.map(savedEntity)).thenReturn(expectedUser);

    User result = repository.execute(user);

    assertThat(result).isEqualTo(expectedUser);
    verify(toEntityMapper).map(user);
    verify(userRepository).save(mappedEntity);
    verify(toDomainMapper).map(savedEntity);
  }

  @Test
  @DisplayName("should map user to entity before saving")
  void shouldMapUserToEntityBeforeSaving() {
    User user = new User(UUID.randomUUID(), "test", "test@example.com", "secret", null, null,
        null, null, true);
    UserEntity userEntity = new UserEntity(
        user.id(), "test", "test@example.com", "secret", null, null, null, true
    );

    when(toEntityMapper.map(user)).thenReturn(userEntity);
    when(userRepository.save(userEntity)).thenReturn(userEntity);
    when(toDomainMapper.map(userEntity)).thenReturn(user);

    repository.execute(user);

    verify(toEntityMapper).map(user);
    verify(userRepository).save(userEntity);
  }

  @Test
  @DisplayName("should return mapped domain user after saving entity")
  void shouldReturnMappedUserAfterSave() {
    UUID userId = UUID.randomUUID();
    User domainUser = new User(userId, "test", "test@example.com", "pass", null, null, null, null,
        true);

    UserEntity entityToSave = new UserEntity(
        userId, "test", "test@example.com", "pass", null, null, null, true
    );

    UserEntity savedEntity = new UserEntity(
        userId, "test", "test@example.com", "pass", null, null, null, true
    );

    User expectedUser = new User(userId, "test", "test@example.com", "pass", null, null, null, null,
        true);

    when(toEntityMapper.map(domainUser)).thenReturn(entityToSave);
    when(userRepository.save(entityToSave)).thenReturn(savedEntity);
    when(toDomainMapper.map(savedEntity)).thenReturn(expectedUser);

    User result = repository.execute(domainUser);

    assertThat(result).isEqualTo(expectedUser);
    verify(toDomainMapper).map(savedEntity);
  }
}
