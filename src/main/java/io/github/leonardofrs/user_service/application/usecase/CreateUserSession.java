package io.github.leonardofrs.user_service.application.usecase;

import io.github.leonardofrs.user_service.domain.model.User;
import io.github.leonardofrs.user_service.domain.model.UserSession;

public interface CreateUserSession {

  UserSession execute(User user);
}
