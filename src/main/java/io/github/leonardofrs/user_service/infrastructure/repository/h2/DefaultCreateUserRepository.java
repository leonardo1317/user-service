package io.github.leonardofrs.user_service.infrastructure.repository.h2;

import io.github.leonardofrs.user_service.domain.model.User;
import io.github.leonardofrs.user_service.domain.repository.CreateUserRepository;
import io.github.leonardofrs.user_service.infrastructure.mapper.UserEntityToUserMapper;
import io.github.leonardofrs.user_service.infrastructure.mapper.UserToUserEntityMapper;
import org.springframework.stereotype.Component;

@Component
public class DefaultCreateUserRepository implements CreateUserRepository {

  private final UserToUserEntityMapper userToUserEntityMapper;
  private final UserEntityToUserMapper userEntityToUserMapper;
  private final UserRepository userRepository;

  public DefaultCreateUserRepository(UserToUserEntityMapper userToUserEntityMapper,
      UserEntityToUserMapper userEntityToUserMapper, UserRepository userRepository) {
    this.userToUserEntityMapper = userToUserEntityMapper;
    this.userEntityToUserMapper = userEntityToUserMapper;
    this.userRepository = userRepository;
  }

  @Override
  public User execute(User user) {
    var entity = userToUserEntityMapper.map(user);
    var savedEntity = userRepository.save(entity);
    return userEntityToUserMapper.map(savedEntity);
  }
}
