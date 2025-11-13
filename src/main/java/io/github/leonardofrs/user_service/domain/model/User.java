package io.github.leonardofrs.user_service.domain.model;

import static java.util.Objects.requireNonNullElse;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public record User(
    UUID id,
    String name,
    String email,
    String password,
    List<Phone> phones,
    Instant created,
    Instant modified,
    Instant lastLogin,
    boolean isActive
) {

  public static User create(
      String name,
      String email,
      String password,
      List<Phone> phones
  ) {
    Instant now = Instant.now();
    return create(
        UUID.randomUUID(),
        name,
        email,
        password,
        phones,
        now,
        now,
        now,
        true
    );
  }

  public static User create(
      UUID id,
      String name,
      String email,
      String password,
      List<Phone> phones,
      Instant created,
      Instant modified,
      Instant lastLogin,
      boolean isActive
  ) {

    return new User(
        id,
        name,
        email,
        password,
        List.copyOf(requireNonNullElse(phones, Collections.emptyList())),
        created,
        modified,
        lastLogin,
        isActive
    );
  }

  public User updatePassword(String encryptedPassword) {
    return new User(
        id,
        name,
        email,
        encryptedPassword,
        phones,
        created,
        Instant.now(),
        lastLogin,
        isActive
    );
  }
}
