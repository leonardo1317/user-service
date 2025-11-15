package io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller.contract;

import jakarta.validation.constraints.NotBlank;

public record PhoneRequest(
    @NotBlank String number,
    String cityCode,
    String countryCode
) {

}
