package io.github.leonardofrs.user_service.domain.repository;

import io.github.leonardofrs.user_service.domain.model.User;

public interface CreateUserRepository {

  User execute(User user);
}
