package io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller.contract;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record UserRequest(
    @NotBlank String name,
    @Email @NotBlank String email,
    @NotBlank String password,
    List<PhoneRequest> phones
) {

}
