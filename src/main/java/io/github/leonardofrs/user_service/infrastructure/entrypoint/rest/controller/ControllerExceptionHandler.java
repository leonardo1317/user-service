package io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNullElse;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.github.leonardofrs.user_service.application.usecase.exception.InvalidEmailException;
import io.github.leonardofrs.user_service.application.usecase.exception.InvalidPasswordException;
import io.github.leonardofrs.user_service.infrastructure.entrypoint.rest.controller.contract.ApiError;
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
  public ResponseEntity<ApiError> handleGeneralException(Exception ex) {
    log.error("Error interno del servidor", ex);
    var error = new ApiError("error interno del servidor");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ApiError> handleNotFound(NoSuchElementException ex) {
    var mensaje = requireNonNullElse(ex.getMessage(), "recurso no encontrado");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(mensaje));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException ex) {
    var mensaje = ex.getBindingResult().getFieldErrors().stream()
        .findFirst()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .orElse("datos inválidos");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(mensaje));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
    var mensaje = requireNonNullElse(ex.getMessage(), "parámetros inválidos");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(mensaje));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
    var mensaje = "violación de integridad de datos";

    if (ex.getCause() instanceof ConstraintViolationException constraintEx) {
      if (nonNull(constraintEx.getMessage()) && constraintEx.getMessage().toLowerCase()
          .contains("email")) {
        mensaje = "El correo ya registrado";
      }
    }

    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiError(mensaje));
  }

  @ExceptionHandler(InvalidPasswordException.class)
  public ResponseEntity<ApiError> handleInvalidPassword(InvalidPasswordException ex) {
    var mensaje = requireNonNullElse(ex.getMessage(), "el password proporcionada es inválido");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(mensaje));
  }

  @ExceptionHandler(InvalidEmailException.class)
  public ResponseEntity<ApiError> handleInvalidPassword(InvalidEmailException ex) {
    var mensaje = requireNonNullElse(ex.getMessage(), "el email proporcionada es inválido");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(mensaje));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiError> handleJsonParseError(HttpMessageNotReadableException ex) {
    Throwable cause = ex.getCause();

    if (cause instanceof UnrecognizedPropertyException unrecognized) {
      String field = unrecognized.getPropertyName();
      String mensaje = String.format("propiedad '%s' no reconocida en la solicitud", field);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(mensaje));
    }

    var mensaje = "error en el formato del JSON enviado";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiError(mensaje));
  }
}
