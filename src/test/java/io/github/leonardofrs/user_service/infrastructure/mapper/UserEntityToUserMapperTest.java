package io.github.leonardofrs.user_service.infrastructure.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
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

class UserEntityToUserMapperTest {

  private PhoneEntityToPhoneMapper phoneEntityToPhoneMapper;
  private UserEntityToUserMapper mapper;

  @BeforeEach
  void setUp() {
    phoneEntityToPhoneMapper = mock(PhoneEntityToPhoneMapper.class);
    mapper = new UserEntityToUserMapper(phoneEntityToPhoneMapper);
  }

  @Test
  @DisplayName("should map UserEntity to User successfully")
  void shouldMapUserEntityToUserSuccessfully() {
    UUID userId = UUID.randomUUID();
    List<PhoneEntity> phoneEntities = List.of(
        new PhoneEntity(UUID.randomUUID(), "123456", "1", "57")
    );
    List<Phone> mappedPhones = List.of(
        new Phone(UUID.randomUUID(), "123456", "1", "57")
    );

    UserEntity userEntity = new UserEntity(
        userId,
        "test",
        "test@example.com",
        "",
        LocalDateTime.now(),
        LocalDateTime.now(),
        LocalDateTime.now(),
        true);

    userEntity.addPhones(phoneEntities);

    when(phoneEntityToPhoneMapper.map(phoneEntities)).thenReturn(mappedPhones);

    User result = mapper.map(userEntity);

    assertNotNull(result);
    assertEquals(userId, result.id());
    assertEquals("test", result.name());
    assertEquals("test@example.com", result.email());
    assertEquals(mappedPhones, result.phones());
    assertTrue(result.isActive());
    assertNotNull(result.created());
    assertNotNull(result.modified());
    assertNotNull(result.lastLogin());
  }

  @Test
  @DisplayName("should throw NullPointerException when source is null")
  void shouldThrowExceptionWhenSourceIsNull() {
    assertThrows(NullPointerException.class, () -> mapper.map((UserEntity) null));
  }
}
