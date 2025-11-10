package io.github.leonardofrs.user_service.infrastructure.mapper;

import static java.util.Objects.requireNonNull;

import io.github.leonardofrs.user_service.domain.model.Phone;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.PhoneEntity;
import org.springframework.stereotype.Component;

@Component
public class PhoneEntityToPhoneMapper implements Mapper<PhoneEntity, Phone> {

  @Override
  public Phone map(PhoneEntity source) {
    requireNonNull(source);
    return new Phone(
        source.getId(),
        source.getNumber(),
        source.getCityCode(),
        source.getCountryCode()
    );
  }
}
