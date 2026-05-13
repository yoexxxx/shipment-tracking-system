package ru.filimonov.eventhandlerservice.exceptionhandler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.filimonov.eventhandlerservice.exceptionhandler.dto.response.ErrorResponse;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFound(EntityNotFoundException exception){
    var errorResponse = ErrorResponse.builder()
                                     .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                                     .errorMessage(exception.getMessage())
                                     .timestamp(LocalDateTime.now())
                                     .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException exception){
    var message = exception.getConstraintViolations()
                           .stream()
                           .map(violation -> violation.getMessage())
                           .collect(Collectors.joining());
    var errorResponse = ErrorResponse.builder()
                                     .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                                     .errorMessage(message)
                                     .timestamp(LocalDateTime.now())
                                     .build();
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }
}
