package io.github.leonardofrs.user_service.infrastructure.repository.h2;

import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.UserSessionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSessionEntity, String> {

}
