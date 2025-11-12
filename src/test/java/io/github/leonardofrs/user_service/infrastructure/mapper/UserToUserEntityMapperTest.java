package io.github.leonardofrs.user_service.infrastructure.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.leonardofrs.user_service.domain.model.Phone;
import io.github.leonardofrs.user_service.domain.model.User;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.PhoneEntity;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.UserEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserToUserEntityMapperTest {

  private PhoneToPhoneEntityMapper phoneToPhoneEntityMapper;
  private UserToUserEntityMapper mapper;

  @BeforeEach
  void setUp() {
    phoneToPhoneEntityMapper = mock(PhoneToPhoneEntityMapper.class);
    mapper = new UserToUserEntityMapper(phoneToPhoneEntityMapper);
  }

  @Test
  @DisplayName("should map User to UserEntity successfully")
  void shouldMapUserToUserEntitySuccessfully() {
    UUID userId = UUID.randomUUID();
    LocalDateTime now = LocalDateTime.now();

    Phone phone = new Phone(UUID.randomUUID(), "123456", "1", "57");
    PhoneEntity phoneEntity = new PhoneEntity(phone.id(), phone.number(), phone.cityCode(),
        phone.countryCode());
    when(phoneToPhoneEntityMapper.map(anyList())).thenReturn(List.of(phoneEntity));

    User user = User.create(
        userId,
        "test",
        "test@example.com",
        "password123",
        List.of(phone),
        now,
        now,
        now,
        true
    );

    UserEntity result = mapper.map(user);

    assertNotNull(result);
    assertEquals(userId, result.getId());
    assertEquals("test", result.getName());
    assertEquals("test@example.com", result.getEmail());
    assertEquals(now, result.getCreated());
    assertEquals(now, result.getModified());
    assertEquals(now, result.getLastLogin());
    assertTrue(result.isActive());
    assertEquals(1, result.getPhones().size());
    assertEquals("123456", result.getPhones().get(0).getNumber());

    verify(phoneToPhoneEntityMapper).map(user.phones());
  }

  @Test
  @DisplayName("should throw NullPointerException when User is null")
  void shouldThrowExceptionWhenUserIsNull() {
    assertThrows(NullPointerException.class, () -> mapper.map((User) null));
  }
}