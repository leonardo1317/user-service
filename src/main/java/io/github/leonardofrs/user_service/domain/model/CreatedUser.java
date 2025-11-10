package io.github.leonardofrs.user_service.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreatedUser(
    UUID id,
    String name,
    String email,
    List<Phone> phones,
    LocalDateTime created,
    LocalDateTime modified,
    LocalDateTime lastLogin,
    String token,
    boolean isActive
) {

}
