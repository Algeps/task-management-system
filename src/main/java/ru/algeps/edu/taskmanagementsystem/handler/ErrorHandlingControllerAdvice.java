package ru.algeps.edu.taskmanagementsystem.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.algeps.edu.taskmanagementsystem.dto.errormessage.ApiErrorMessage;

@RestControllerAdvice
public class ErrorHandlingControllerAdvice extends ResponseEntityExceptionHandler {
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorMessage> onRuntimeException(Exception e) {
    return ResponseEntity.badRequest().body(new ApiErrorMessage(e.getLocalizedMessage()));
  }
}
