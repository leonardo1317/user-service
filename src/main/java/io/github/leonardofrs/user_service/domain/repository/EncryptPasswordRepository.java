package io.github.leonardofrs.user_service.domain.repository;

public interface EncryptPasswordRepository {

  String execute(String rawPassword);
}
