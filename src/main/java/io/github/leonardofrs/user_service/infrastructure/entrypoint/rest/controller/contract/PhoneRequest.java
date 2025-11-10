package io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller.contract;

import jakarta.validation.constraints.NotBlank;

public record PhoneRequest(
    @NotBlank(message = "Campo 'number' es requerido.") String number,
    String cityCode,
    String countryCode
) {

}
