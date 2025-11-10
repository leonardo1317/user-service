package io.github.leonardofrs.user_service.infrastructure.mapper;

import io.github.leonardofrs.user_service.domain.model.Phone;
import io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller.contract.PhoneRequest;
import org.springframework.stereotype.Component;

@Component
public class PhoneRequestToPhoneMapper implements Mapper<PhoneRequest, Phone> {

  @Override
  public Phone map(PhoneRequest source) {
    return Phone.create(
        source.number(),
        source.cityCode(),
        source.countryCode()
    );
  }
}
