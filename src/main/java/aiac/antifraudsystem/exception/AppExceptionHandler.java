package aiac.antifraudsystem.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public AntiFraudException handleConstraintValidationException(
            ConstraintViolationException ex, WebRequest request) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .toList();
        return new AntiFraudException(BAD_REQUEST, errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public AntiFraudException handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = ex.getFieldErrors().stream()
                .map(fe -> "%s %s".formatted(fe.getField(), fe.getDefaultMessage()))
                .toList();
        return new AntiFraudException(BAD_REQUEST, errors);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<AntiFraudException> handleResponseException(
            ResponseStatusException ex, WebRequest request) {
        String message = Objects.requireNonNullElse(ex.getReason(), ex.getMessage());
        AntiFraudException exception = new AntiFraudException((HttpStatus) ex.getStatusCode(), List.of(message));
        return ResponseEntity.status(ex.getStatusCode()).body(exception);
    }
}
