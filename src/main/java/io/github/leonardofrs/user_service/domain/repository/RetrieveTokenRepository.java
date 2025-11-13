package io.github.leonardofrs.user_service.domain.repository;

import io.github.leonardofrs.user_service.domain.dto.Token;

public interface RetrieveTokenRepository {

  Token execute(String userId, String email);
}
