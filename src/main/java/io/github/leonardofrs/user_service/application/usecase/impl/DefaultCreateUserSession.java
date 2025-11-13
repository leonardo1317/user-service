package io.github.leonardofrs.user_service.application.usecase.impl;

import io.github.leonardofrs.user_service.application.usecase.CreateUserSession;
import io.github.leonardofrs.user_service.domain.dto.Token;
import io.github.leonardofrs.user_service.domain.model.User;
import io.github.leonardofrs.user_service.domain.model.UserSession;
import io.github.leonardofrs.user_service.domain.repository.CreateUserSessionRepository;
import io.github.leonardofrs.user_service.domain.repository.RetrieveTokenRepository;

public class DefaultCreateUserSession implements CreateUserSession {

  private final RetrieveTokenRepository retrieveTokenRepository;
  private final CreateUserSessionRepository createUserSessionRepository;

  public DefaultCreateUserSession(RetrieveTokenRepository retrieveTokenRepository,
      CreateUserSessionRepository createUserSessionRepository) {
    this.retrieveTokenRepository = retrieveTokenRepository;
    this.createUserSessionRepository = createUserSessionRepository;
  }

  @Override
  public UserSession execute(User user) {
    Token token = retrieveTokenRepository.execute(user.id().toString(), user.email());
    var userSession = UserSession.create(token.value(), token.issuedAt(),
        token.expiresAt(), user.id());
    return createUserSessionRepository.execute(userSession);
  }
}
