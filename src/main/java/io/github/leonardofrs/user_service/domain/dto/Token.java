package io.github.leonardofrs.user_service.domain.dto;

import java.time.Instant;

public record Token(String value,
                    Instant issuedAt,
                    Instant expiresAt) {

}
