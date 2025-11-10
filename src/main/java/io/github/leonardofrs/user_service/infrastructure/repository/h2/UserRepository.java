package io.github.leonardofrs.user_service.infrastructure.repository.h2;

import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {

}
