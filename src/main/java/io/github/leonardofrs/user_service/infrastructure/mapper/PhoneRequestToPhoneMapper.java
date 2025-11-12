package io.github.leonardofrs.user_service.infrastructure.mapper;

import static java.util.Objects.requireNonNull;

import io.github.leonardofrs.user_service.domain.model.Phone;
import io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller.contract.PhoneRequest;
import org.springframework.stereotype.Component;

@Component
public class PhoneRequestToPhoneMapper implements Mapper<PhoneRequest, Phone> {

  @Override
  public Phone map(PhoneRequest source) {
    requireNonNull(source, "PhoneRequest no puede ser nulo");
    return Phone.create(
        source.number(),
        source.cityCode(),
        source.countryCode()
    );
  }
}
