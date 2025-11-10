package io.github.leonardofrs.user_service.application.usecase.impl;

import io.github.leonardofrs.user_service.application.usecase.CreateUser;
import io.github.leonardofrs.user_service.application.usecase.ValidateEmail;
import io.github.leonardofrs.user_service.application.usecase.ValidatePassword;
import io.github.leonardofrs.user_service.domain.model.User;
import io.github.leonardofrs.user_service.domain.repository.CreateUserRepository;
import io.github.leonardofrs.user_service.domain.repository.EncryptPasswordRepository;

public class DefaultCreateUser implements CreateUser {

  private final ValidateEmail validateEmail;
  private final ValidatePassword validatePassword;
  private final EncryptPasswordRepository encryptPasswordRepository;
  private final CreateUserRepository createUserRepository;

  public DefaultCreateUser(ValidateEmail validateEmail, ValidatePassword validatePassword,
      EncryptPasswordRepository encryptPasswordRepository,
      CreateUserRepository createUserRepository) {
    this.validateEmail = validateEmail;
    this.validatePassword = validatePassword;
    this.encryptPasswordRepository = encryptPasswordRepository;
    this.createUserRepository = createUserRepository;
  }

  @Override
  public User execute(User user) {
    validateEmail.execute(user.email());
    validatePassword.execute(user.password());
    String encryptedPassword = encryptPasswordRepository.execute(user.password());
    User userWithEncryptedPassword = user.updatePassword(encryptedPassword);
    return createUserRepository.execute(userWithEncryptedPassword);
  }
}
