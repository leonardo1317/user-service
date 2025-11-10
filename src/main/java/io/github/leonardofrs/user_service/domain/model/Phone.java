package io.github.leonardofrs.user_service.domain.model;

import java.util.UUID;

public record Phone(
    UUID id,
    String number,
    String cityCode,
    String countryCode
) {

  public static Phone create(String number, String cityCode, String countryCode) {
    return create(UUID.randomUUID(), number, cityCode, countryCode);
  }

  public static Phone create(UUID id, String number, String cityCode, String countryCode) {
    return new Phone(id, number, cityCode, countryCode);
  }
}
