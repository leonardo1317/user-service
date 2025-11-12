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

class PhoneEntityToPhoneMapperTest {

  private PhoneEntityToPhoneMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new PhoneEntityToPhoneMapper();
  }

  @Test
  @DisplayName("should map all fields correctly from PhoneEntity to Phone")
  void shouldMapAllFieldsCorrectlyFromPhoneEntityToPhone() {
    UUID id = UUID.randomUUID();
    String number = "123456789";
    String cityCode = "1";
    String countryCode = "57";

    PhoneEntity entity = new PhoneEntity(id, number, cityCode, countryCode);

    Phone phone = mapper.map(entity);

    assertNotNull(phone);
    assertEquals(id, phone.id());
    assertEquals(number, phone.number());
    assertEquals(cityCode, phone.cityCode());
    assertEquals(countryCode, phone.countryCode());
  }

  @Test
  @DisplayName("should throw NullPointerException when source is null")
  void shouldThrowNullPointerExceptionWhenSourceIsNull() {
    assertThrows(NullPointerException.class, () -> mapper.map((PhoneEntity) null));
  }
}