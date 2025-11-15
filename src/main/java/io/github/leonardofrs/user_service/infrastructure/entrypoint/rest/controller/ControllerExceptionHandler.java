package io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNullElse;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.github.leonardofrs.user_service.application.usecase.exception.InvalidEmailException;
import io.github.leonardofrs.user_service.application.usecase.exception.InvalidPasswordException;
import io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller.contract.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGeneralException(Exception ex, HttpServletRequest request) {
    var error = buildError(
        "internal server error",
        requireNonNullElse(ex.getMessage(), "an unexpected error occurred"),
        HttpStatus.INTERNAL_SERVER_ERROR,
        request.getRequestURI()
    );
    log.error(error.message(), ex);
    return ResponseEntity.status(error.status()).body(error);
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ApiError> handleNotFound(NoSuchElementException ex,
      HttpServletRequest request) {

    var error = buildError(
        "resource not found",
        ex.getMessage(),
        HttpStatus.NOT_FOUND,
        request.getRequestURI()
    );
    return ResponseEntity.status(error.status()).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex,
      HttpServletRequest request) {
    var mensaje = ex.getBindingResult().getFieldErrors().stream()
        .findFirst()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .orElse("invalid data");

    var error = buildError(
        "validation error",
        mensaje,
        HttpStatus.BAD_REQUEST,
        request.getRequestURI()
    );

    return ResponseEntity.status(error.status()).body(error);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex,
      HttpServletRequest request) {

    var error = buildError(
        "invalid parameters",
        ex.getMessage(),
        HttpStatus.BAD_REQUEST,
        request.getRequestURI()
    );

    return ResponseEntity.status(error.status()).body(error);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex,
      HttpServletRequest request) {
    var mensaje = "data integrity violation";

    if (ex.getCause() instanceof ConstraintViolationException constraintEx) {
      if (nonNull(constraintEx.getMessage()) && constraintEx.getMessage().toLowerCase()
          .contains("email")) {
        mensaje = "the email is already registered";
      }
    }

    var error = buildError(
        "conflict",
        mensaje,
        HttpStatus.CONFLICT,
        request.getRequestURI()
    );

    return ResponseEntity.status(error.status()).body(error);
  }

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<ApiError> handleInvalidPassword(InvalidPasswordException ex,
      HttpServletRequest request) {

    var error = buildError(
        "invalid password",
        requireNonNullElse(ex.getMessage(), "the provided password is invalid"),
        HttpStatus.BAD_REQUEST,
        request.getRequestURI()
    );

    return ResponseEntity.status(error.status()).body(error);
  }

  @ExceptionHandler(InvalidEmailException.class)
  public ResponseEntity<ApiError> handleInvalidPassword(InvalidEmailException ex,
      HttpServletRequest request) {

    var error = buildError(
        "invalid email",
        requireNonNullElse(ex.getMessage(), "the provided email is invalid"),
        HttpStatus.BAD_REQUEST,
        request.getRequestURI()
    );
    return ResponseEntity.status(error.status()).body(error);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiError> handleJsonParseError(HttpMessageNotReadableException ex,
      HttpServletRequest request) {
    Throwable cause = ex.getCause();
    var title = "invalid json";
    var httpStatus = HttpStatus.BAD_REQUEST;
    if (cause instanceof UnrecognizedPropertyException unrecognized) {
      String field = unrecognized.getPropertyName();
      String mensaje = String.format("unrecognized property '%s' in the request", field);
      var error = buildError(title, mensaje, httpStatus,
          request.getRequestURI());
      return ResponseEntity.status(error.status()).body(error);
    }

    var error = buildError(title, "invalid JSON format", httpStatus,
        request.getRequestURI());
    return ResponseEntity.status(error.status()).body(error);
  }

  private ApiError buildError(String title, String detail, HttpStatus status, String path) {
    return new ApiError(title, detail, status.value(), path);
  }
}
