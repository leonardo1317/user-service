package io.github.leonardofrs.user_service.domain.repository;

public interface RetrieveTokenRepository {

  String execute(String userId, String email);
}
