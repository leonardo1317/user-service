package io.github.leonardofrs.user_service.infrastructure.mapper;

import static java.util.Objects.requireNonNull;

import io.github.leonardofrs.user_service.domain.model.Phone;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.PhoneEntity;
import org.springframework.stereotype.Component;

@Component
public class PhoneToPhoneEntityMapper implements Mapper<Phone, PhoneEntity> {

  @Override
  public PhoneEntity map(Phone source) {
    requireNonNull(source, "source must not be null");
    return new PhoneEntity(
        source.id(),
        source.number(),
        source.cityCode(),
        source.countryCode()
    );
  }
}
