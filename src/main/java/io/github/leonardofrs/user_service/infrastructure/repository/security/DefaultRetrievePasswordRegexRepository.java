package io.github.leonardofrs.user_service.infrastructure.repository.security;

import io.github.leonardofrs.user_service.domain.repository.RetrievePasswordRegexRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultRetrievePasswordRegexRepository implements RetrievePasswordRegexRepository {

  private final String passwordRegex;

  public DefaultRetrievePasswordRegexRepository(
      @Value("${security.password-regex}") String passwordRegex) {
    this.passwordRegex = passwordRegex;
  }

  @Override
  public String execute() {
    return passwordRegex;
  }
}
