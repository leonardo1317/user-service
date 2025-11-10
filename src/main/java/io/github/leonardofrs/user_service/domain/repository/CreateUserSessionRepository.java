package io.github.leonardofrs.user_service.domain.repository;

import io.github.leonardofrs.user_service.domain.model.UserSession;

public interface CreateUserSessionRepository {

  UserSession execute(UserSession userSession);
}
