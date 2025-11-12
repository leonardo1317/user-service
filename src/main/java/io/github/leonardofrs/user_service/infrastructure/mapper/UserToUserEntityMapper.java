package io.github.leonardofrs.user_service.infrastructure.mapper;

import static java.util.Objects.requireNonNull;

import io.github.leonardofrs.user_service.domain.model.User;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.PhoneEntity;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.UserEntity;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UserToUserEntityMapper implements Mapper<User, UserEntity> {

  private final PhoneToPhoneEntityMapper phoneToPhoneEntityMapper;

  public UserToUserEntityMapper(PhoneToPhoneEntityMapper phoneToPhoneEntityMapper) {
    this.phoneToPhoneEntityMapper = phoneToPhoneEntityMapper;
  }

  @Override
  public UserEntity map(User source) {
    requireNonNull(source, "User no puede ser nulo");
    var userEntity = new UserEntity(
        source.id(),
        source.name(),
        source.email(),
        source.password(),
        source.created(),
        source.modified(),
        source.lastLogin(),
        source.isActive()
    );

    List<PhoneEntity> phoneEntities = phoneToPhoneEntityMapper.map(source.phones());

    userEntity.addPhones(phoneEntities);
    return userEntity;
  }
}
