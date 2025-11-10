package io.github.leonardofrs.user_service.infrastructure.repository.security;

import io.github.leonardofrs.user_service.domain.repository.RetrievePasswordRegexRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DefaultRetrievePasswordRegexRepository implements RetrievePasswordRegexRepository {

  @Value("${app.security.password-regex}")
  private String passwordRegex;

  @Override
  public String execute() {
    return passwordRegex;
  }
}
