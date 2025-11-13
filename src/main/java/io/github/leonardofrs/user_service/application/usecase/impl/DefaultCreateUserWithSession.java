package io.github.leonardofrs.user_service.application.usecase.impl;

import io.github.leonardofrs.user_service.application.usecase.CreateUser;
import io.github.leonardofrs.user_service.application.usecase.CreateUserSession;
import io.github.leonardofrs.user_service.application.usecase.CreateUserWithSession;
import io.github.leonardofrs.user_service.domain.dto.CreatedUser;
import io.github.leonardofrs.user_service.domain.model.User;

public class DefaultCreateUserWithSession implements CreateUserWithSession {

  private final CreateUser createUser;
  private final CreateUserSession createUserSession;

  public DefaultCreateUserWithSession(CreateUser createUser, CreateUserSession createUserSession) {
    this.createUser = createUser;
    this.createUserSession = createUserSession;
  }

  @Override
  public CreatedUser execute(User user) {
    var newUser = createUser.execute(user);
    var userSession = createUserSession.execute(newUser);
    return new CreatedUser(
        newUser.id(),
        newUser.name(),
        newUser.email(),
        newUser.phones(),
        newUser.created(),
        newUser.modified(),
        newUser.lastLogin(),
        userSession.token(),
        newUser.isActive()
    );
  }
}
