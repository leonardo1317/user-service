package io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller.contract;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record UserRequest(
    @NotBlank(message = "Campo 'name' es requerido.") String name,
    @Email(message = "El formato del email no es correcto.")
    @NotBlank(message = "Campo 'email' es requerido.") String email,
    @NotBlank(message = "Campo 'password' es requerido.") String password,
    @Valid List<PhoneRequest> phones
) {

}
