package io.github.leonardofrs.user_service.infrastructure.mapper;

import static java.util.Objects.requireNonNull;

import io.github.leonardofrs.user_service.domain.model.User;
import io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller.contract.UserRequest;
import org.springframework.stereotype.Component;

@Component
public class UserRequestToUserMapper implements Mapper<UserRequest, User> {

  private final PhoneRequestToPhoneMapper phoneRequestToPhoneMapper;

  public UserRequestToUserMapper(PhoneRequestToPhoneMapper phoneRequestToPhoneMapper) {
    this.phoneRequestToPhoneMapper = phoneRequestToPhoneMapper;
  }

  @Override
  public User map(UserRequest source) {
    requireNonNull(source, "UserRequest no puede ser nulo");
    return User.create(
        source.name(),
        source.email(),
        source.password(),
        phoneRequestToPhoneMapper.map(source.phones())
    );
  }
}
