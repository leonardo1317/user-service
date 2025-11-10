package io.github.leonardofrs.user_service.application.usecase;

import io.github.leonardofrs.user_service.domain.model.User;

public interface CreateUser {

  User execute(User user);
}
