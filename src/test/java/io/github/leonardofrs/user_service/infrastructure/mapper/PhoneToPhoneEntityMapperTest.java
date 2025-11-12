package io.github.leonardofrs.user_service.infrastructure.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.leonardofrs.user_service.domain.model.Phone;
import io.github.leonardofrs.user_service.infrastructure.repository.h2.entity.PhoneEntity;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PhoneToPhoneEntityMapperTest {

  private PhoneToPhoneEntityMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new PhoneToPhoneEntityMapper();
  }

  @Test
  @DisplayName("should map Phone to PhoneEntity successfully")
  void shouldMapPhoneToPhoneEntitySuccessfully() {
    UUID id = UUID.randomUUID();
    Phone phone = new Phone(id, "123456", "1", "57");

    PhoneEntity result = mapper.map(phone);

    assertNotNull(result);
    assertEquals(id, result.getId());
    assertEquals("123456", result.getNumber());
    assertEquals("1", result.getCityCode());
    assertEquals("57", result.getCountryCode());
  }

  @Test
  @DisplayName("should throw NullPointerException when source is null")
  void shouldThrowExceptionWhenSourceIsNull() {
    assertThrows(NullPointerException.class, () -> mapper.map((Phone) null));
  }
}
