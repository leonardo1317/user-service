package io.github.leonardofrs.user_service.infrastructure.repository.security;

import io.github.leonardofrs.user_service.domain.repository.EncryptPasswordRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultEncryptPasswordRepository implements EncryptPasswordRepository {

  private final PasswordEncoder passwordEncoder;

  public DefaultEncryptPasswordRepository(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public String execute(String rawPassword) {
    return passwordEncoder.encode(rawPassword);
  }
}
