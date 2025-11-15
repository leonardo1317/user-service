package io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller.contract;

public record PhoneRequest(
    String number,
    String cityCode,
    String countryCode
) {

}
