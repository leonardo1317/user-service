package io.github.leonardofrs.user_service.infrastructure.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.leonardofrs.user_service.domain.model.Phone;
import io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller.contract.PhoneRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PhoneRequestToPhoneMapperTest {

  private PhoneRequestToPhoneMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new PhoneRequestToPhoneMapper();
  }

  @Test
  @DisplayName("should map PhoneRequest to Phone successfully")
  void shouldMapPhoneRequestToPhoneSuccessfully() {
    PhoneRequest request = new PhoneRequest("123456", "1", "57");

    Phone result = mapper.map(request);

    assertNotNull(result);
    assertEquals("123456", result.number());
    assertEquals("1", result.cityCode());
    assertEquals("57", result.countryCode());
  }

  @Test
  @DisplayName("should throw NullPointerException when source is null")
  void shouldThrowExceptionWhenSourceIsNull() {
    assertThrows(NullPointerException.class, () -> mapper.map((PhoneRequest) null));
  }

}