package io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import io.github.leonardofrs.user_service.application.usecase.CreateUserWithSession;
import io.github.leonardofrs.user_service.domain.dto.CreatedUser;
import io.github.leonardofrs.user_service.domain.model.User;
import io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller.contract.UserRequest;
import io.github.leonardofrs.user_service.infrastructure.mapper.UserRequestToUserMapper;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class UserControllerTest {

  private UserRequestToUserMapper userRequestToUserMapper;
  private CreateUserWithSession createUserWithSession;
  private UserController userController;

  @BeforeEach
  void setUp() {
    userRequestToUserMapper = mock(UserRequestToUserMapper.class);
    createUserWithSession = mock(CreateUserWithSession.class);
    userController = new UserController(userRequestToUserMapper, createUserWithSession);
  }

  @Test
  @DisplayName("should map user request to domain user and create user with session")
  void shouldMapAndCreateUserWithSession() {
    UserRequest userRequest = new UserRequest(
        "test",
        "test@example.com",
        "rawPassword123",
        List.of()
    );

    User mappedUser = new User(
        UUID.randomUUID(),
        userRequest.name(),
        userRequest.email(),
        userRequest.password(),
        Collections.emptyList(),
        Instant.now(),
        Instant.now(),
        null,
        true
    );

    CreatedUser createdUser = new CreatedUser(
        mappedUser.id(),
        mappedUser.name(),
        mappedUser.email(),
        mappedUser.phones(),
        mappedUser.created(),
        mappedUser.modified(),
        mappedUser.lastLogin(),
        "jwt-token",
        mappedUser.isActive()
    );

    when(userRequestToUserMapper.map(userRequest)).thenReturn(mappedUser);
    when(createUserWithSession.execute(mappedUser)).thenReturn(createdUser);

    ResponseEntity<CreatedUser> response = userController.create(userRequest);

    assertThat(response).isNotNull();
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isEqualTo(createdUser);

    verify(userRequestToUserMapper).map(userRequest);
    verify(createUserWithSession).execute(mappedUser);
  }

  @Test
  @DisplayName("should throw exception if user request mapping fails")
  void shouldThrowExceptionIfMappingFails() {
    UserRequest userRequest = new UserRequest(
        "test",
        "invalid-email",
        "pass",
        Collections.emptyList()
    );

    when(userRequestToUserMapper.map(userRequest)).thenThrow(
        new RuntimeException("Mapping failed"));

    assertThatThrownBy(() -> userController.create(userRequest))
        .isInstanceOf(RuntimeException.class)
        .hasMessage("Mapping failed");

    verify(userRequestToUserMapper).map(userRequest);
    verifyNoInteractions(createUserWithSession);
  }
}