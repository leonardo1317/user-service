package io.github.leonardofrs.user_service.infrastructure.config;

import io.github.leonardofrs.user_service.application.usecase.CreateUser;
import io.github.leonardofrs.user_service.application.usecase.CreateUserSession;
import io.github.leonardofrs.user_service.application.usecase.CreateUserWithSession;
import io.github.leonardofrs.user_service.application.usecase.ValidateEmail;
import io.github.leonardofrs.user_service.application.usecase.ValidatePassword;
import io.github.leonardofrs.user_service.application.usecase.impl.DefaultCreateUser;
import io.github.leonardofrs.user_service.application.usecase.impl.DefaultCreateUserSession;
import io.github.leonardofrs.user_service.application.usecase.impl.DefaultCreateUserWithSession;
import io.github.leonardofrs.user_service.application.usecase.impl.DefaultValidateEmail;
import io.github.leonardofrs.user_service.application.usecase.impl.DefaultValidatePassword;
import io.github.leonardofrs.user_service.domain.repository.CreateUserRepository;
import io.github.leonardofrs.user_service.domain.repository.CreateUserSessionRepository;
import io.github.leonardofrs.user_service.domain.repository.EncryptPasswordRepository;
import io.github.leonardofrs.user_service.domain.repository.RetrievePasswordRegexRepository;
import io.github.leonardofrs.user_service.domain.repository.RetrieveTokenRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseBeansConfig {

  @Bean
  public CreateUserWithSession createUserWithSession(CreateUser createUser,
      CreateUserSession createUserSession) {
    return new DefaultCreateUserWithSession(createUser, createUserSession);
  }

  @Bean
  public CreateUser createUser(ValidateEmail validateEmail, ValidatePassword validatePassword,
      EncryptPasswordRepository encryptPasswordRepository,
      CreateUserRepository createUserRepository
  ) {
    return new DefaultCreateUser(validateEmail, validatePassword, encryptPasswordRepository,
        createUserRepository);
  }

  @Bean
  public ValidatePassword validatePassword(
      RetrievePasswordRegexRepository retrievePasswordRegexRepository) {
    return new DefaultValidatePassword(retrievePasswordRegexRepository);
  }

  @Bean
  public ValidateEmail validateEmail() {
    return new DefaultValidateEmail();
  }

  @Bean
  public CreateUserSession createUserSession(RetrieveTokenRepository retrieveTokenRepository,
      CreateUserSessionRepository createUserSessionRepository) {
    return new DefaultCreateUserSession(retrieveTokenRepository, createUserSessionRepository);
  }
}
