package io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.github.leonardofrs.user_service.application.usecase.exception.InvalidEmailException;
import io.github.leonardofrs.user_service.application.usecase.exception.InvalidPasswordException;
import io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller.contract.ApiError;
import java.util.List;
import java.util.NoSuchElementException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class ControllerExceptionHandlerTest {

  private ControllerExceptionHandler handler;

  @BeforeEach
  void setUp() {
    handler = new ControllerExceptionHandler();
  }

  @Test
  @DisplayName("should return 500 Internal Server Error when a general exception occurs")
  void shouldReturnInternalServerErrorWhenGeneralExceptionOccurs() {
    var ex = new Exception("Unexpected error");

    ResponseEntity<ApiError> response = handler.handleGeneralException(ex);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("internal server error", response.getBody().message());
  }

  @Test
  @DisplayName("should return 404 Not Found when NoSuchElementException is thrown")
  void shouldReturnNotFoundWhenNoSuchElementExceptionIsThrown() {
    var ex = new NoSuchElementException("user not found");

    ResponseEntity<ApiError> response = handler.handleNotFound(ex);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("user not found", response.getBody().message());
  }

  @Test
  @DisplayName("should return default 404 message when NoSuchElementException message is null")
  void shouldReturnDefaultNotFoundMessageWhenExceptionMessageIsNull() {
    var ex = new NoSuchElementException((Throwable) null);

    ResponseEntity<ApiError> response = handler.handleNotFound(ex);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("resource not found", response.getBody().message());
  }

  @Test
  @DisplayName("should return 400 Bad Request when validation error occurs")
  void shouldReturnBadRequestWhenValidationErrorOccurs() {
    var fieldError = mock(FieldError.class);
    when(fieldError.getDefaultMessage()).thenReturn("name is required");

    var bindingResult = mock(BindingResult.class);
    when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

    var ex = new MethodArgumentNotValidException(null, bindingResult);

    ResponseEntity<ApiError> response = handler.handleValidationErrors(ex);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("name is required", response.getBody().message());
  }

  @Test
  @DisplayName("should return default message when no field errors found")
  void shouldReturnDefaultMessageWhenNoFieldErrorsFound() {
    var bindingResult = mock(BindingResult.class);
    when(bindingResult.getFieldErrors()).thenReturn(emptyList());

    var ex = new MethodArgumentNotValidException(null, bindingResult);

    ResponseEntity<ApiError> response = handler.handleValidationErrors(ex);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("invalid data", response.getBody().message());
  }

  @Test
  @DisplayName("should return 400 Bad Request when IllegalArgumentException is thrown")
  void shouldReturnBadRequestWhenIllegalArgumentExceptionIsThrown() {
    var ex = new IllegalArgumentException("the email is invalid");

    ResponseEntity<ApiError> response = handler.handleIllegalArgument(ex);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("the email is invalid", response.getBody().message());
  }

  @Test
  @DisplayName("should return default 400 message when IllegalArgumentException message is null")
  void shouldReturnDefaultBadRequestMessageWhenIllegalArgumentExceptionMessageIsNull() {
    var ex = new IllegalArgumentException((String) null);

    ResponseEntity<ApiError> response = handler.handleIllegalArgument(ex);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("invalid parameters", response.getBody().message());
  }

  @Test
  @DisplayName("should return 409 Conflict when DataIntegrityViolationException with email constraint occurs")
  void shouldReturnConflictWhenDataIntegrityViolationWithEmailOccurs() {
    var cause = mock(ConstraintViolationException.class);
    when(cause.getMessage()).thenReturn("duplicate key value violates unique constraint email");

    var ex = new DataIntegrityViolationException("error", cause);

    ResponseEntity<ApiError> response = handler.handleDataIntegrityViolation(ex);

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("the email is already registered", response.getBody().message());
  }

  @Test
  @DisplayName("should return generic 409 Conflict when DataIntegrityViolationException without email constraint occurs")
  void shouldReturnGenericConflictWhenDataIntegrityViolationWithoutEmailOccurs() {
    var ex = new DataIntegrityViolationException("some db error");

    ResponseEntity<ApiError> response = handler.handleDataIntegrityViolation(ex);

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("data integrity violation", response.getBody().message());
  }

  @Test
  @DisplayName("should return 400 Bad Request when InvalidPasswordException is thrown")
  void shouldReturnBadRequestWhenInvalidPasswordExceptionIsThrown() {
    var ex = new InvalidPasswordException("the password is too weak");

    ResponseEntity<ApiError> response = handler.handleInvalidPassword(ex);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("the password is too weak", response.getBody().message());
  }

  @Test
  @DisplayName("should return 400 Bad Request when InvalidEmailException is thrown")
  void shouldReturnBadRequestWhenInvalidEmailExceptionIsThrown() {
    var ex = new InvalidEmailException("the email is invalid");

    ResponseEntity<ApiError> response = handler.handleInvalidPassword(ex);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("the email is invalid", response.getBody().message());
  }

  @Test
  @DisplayName("should return 400 Bad Request when JSON contains unrecognized property")
  void shouldReturnBadRequestWhenJsonContainsUnrecognizedProperty() {
    var cause = mock(UnrecognizedPropertyException.class);
    when(cause.getPropertyName()).thenReturn("name");

    var ex = new HttpMessageNotReadableException("error", cause, mock(HttpInputMessage.class));

    ResponseEntity<ApiError> response = handler.handleJsonParseError(ex);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("unrecognized property 'name' in the request", response.getBody().message());
  }

  @Test
  @DisplayName("should return 400 Bad Request when JSON format is invalid")
  void shouldReturnBadRequestWhenJsonFormatIsInvalid() {
    var cause = new RuntimeException("parse error");
    var ex = new HttpMessageNotReadableException("error", cause, mock(HttpInputMessage.class));

    ResponseEntity<ApiError> response = handler.handleJsonParseError(ex);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("invalid JSON format", response.getBody().message());
  }
}
