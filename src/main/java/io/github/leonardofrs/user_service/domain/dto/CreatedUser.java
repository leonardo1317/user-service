package io.github.leonardofrs.user_service.domain.dto;

import io.github.leonardofrs.user_service.domain.model.Phone;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CreatedUser(
    UUID id,
    String name,
    String email,
    List<Phone> phones,
    Instant created,
    Instant modified,
    Instant lastLogin,
    String token,
    boolean isActive
) {

}
