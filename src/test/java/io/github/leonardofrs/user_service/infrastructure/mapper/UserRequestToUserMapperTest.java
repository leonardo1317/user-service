package io.github.leonardofrs.user_service.infrastructure.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.github.leonardofrs.user_service.domain.model.Phone;
import io.github.leonardofrs.user_service.domain.model.User;
import io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller.contract.PhoneRequest;
import io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller.contract.UserRequest;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserRequestToUserMapperTest {

  private PhoneRequestToPhoneMapper phoneRequestToPhoneMapper;
  private UserRequestToUserMapper mapper;

  @BeforeEach
  void setUp() {
    phoneRequestToPhoneMapper = mock(PhoneRequestToPhoneMapper.class);
    mapper = new UserRequestToUserMapper(phoneRequestToPhoneMapper);
  }

  @Test
  @DisplayName("should map UserRequest to User successfully")
  void shouldMapUserRequestToUserSuccessfully() {
    List<PhoneRequest> phoneRequests = List.of(
        new PhoneRequest("123456", "1", "57")
    );
    List<Phone> mappedPhones = List.of(
        new Phone(UUID.randomUUID(), "123456", "1", "57")
    );

    UserRequest request = new UserRequest("test", "test@example.com", "securePass123",
        phoneRequests);

    when(phoneRequestToPhoneMapper.map(phoneRequests)).thenReturn(mappedPhones);

    User result = mapper.map(request);

    assertNotNull(result);
    assertEquals("test", result.name());
    assertEquals("test@example.com", result.email());
    assertEquals("securePass123", result.password());
    assertEquals(mappedPhones, result.phones());
  }

  @Test
  @DisplayName("should throw NullPointerException when UserRequest is null")
  void shouldThrowExceptionWhenUserRequestIsNull() {
    assertThrows(NullPointerException.class, () -> mapper.map((UserRequest) null));
  }
}
