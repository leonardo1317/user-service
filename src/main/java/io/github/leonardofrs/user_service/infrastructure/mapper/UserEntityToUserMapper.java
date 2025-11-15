package io.github.leonardofrs.user_service.infrastructure.mapper;

import static java.util.Objects.requireNonNull;

import io.github.leonardofrs.user_service.domain.model.User;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserEntityToUserMapper implements Mapper<UserEntity, User> {

  private final PhoneEntityToPhoneMapper phoneEntityToPhoneMapper;

  public UserEntityToUserMapper(PhoneEntityToPhoneMapper phoneEntityToPhoneMapper) {
    this.phoneEntityToPhoneMapper = phoneEntityToPhoneMapper;
  }

  @Override
  public User map(UserEntity source) {
    requireNonNull(source, "source must not be null");
    return User.create(
        source.getId(),
        source.getName(),
        source.getEmail(),
        "",
        phoneEntityToPhoneMapper.map(source.getPhones()),
        source.getCreated(),
        source.getModified(),
        source.getLastLogin(),
        source.isActive());
  }
}
