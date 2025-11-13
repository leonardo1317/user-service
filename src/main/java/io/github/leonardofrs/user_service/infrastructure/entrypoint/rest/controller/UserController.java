package io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller;

import io.github.leonardofrs.user_service.application.usecase.CreateUserWithSession;
import io.github.leonardofrs.user_service.domain.dto.CreatedUser;
import io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller.contract.UserRequest;
import io.github.leonardofrs.user_service.infrastructure.mapper.UserRequestToUserMapper;
import io.github.leonardofrs.user_service.infrastructure.transaction.TransactionalExecutor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final UserRequestToUserMapper userRequestToUserMapper;
  private final TransactionalExecutor transactionalExecutor;
  private final CreateUserWithSession createUserWithSession;

  public UserController(UserRequestToUserMapper userRequestToUserMapper,
      TransactionalExecutor transactionalExecutor,
      CreateUserWithSession createUserWithSession) {
    this.userRequestToUserMapper = userRequestToUserMapper;
    this.transactionalExecutor = transactionalExecutor;
    this.createUserWithSession = createUserWithSession;
  }

  @PostMapping("/api/v1/users")
  public ResponseEntity<CreatedUser> create(@Valid @RequestBody UserRequest userRequest) {
    CreatedUser createdUser = transactionalExecutor.execute(createUserWithSession::execute,
        userRequestToUserMapper.map(userRequest));
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdUser);
  }
}
