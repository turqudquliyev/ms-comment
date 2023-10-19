package az.ingress.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

import static az.ingress.model.enums.ExceptionMessage.UNEXPECTED_EXCEPTION;
import static az.ingress.model.enums.ExceptionMessage.VALIDATION_EXCEPTION;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ExceptionResponse handle(NotFoundException ex) {
        log.error("NotFoundException: ", ex);
        return ExceptionResponse.builder()
                .code(ex.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ExceptionResponse handle(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException: ", ex);
        List<ValidationException> exception = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ValidationException(fieldError.getDefaultMessage()))
                .toList();
        return ExceptionResponse.builder()
                .code(VALIDATION_EXCEPTION.toString())
                .errors(exception)
                .build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(METHOD_NOT_ALLOWED)
    public ExceptionResponse handle(HttpRequestMethodNotSupportedException ex) {
        log.error("MethodNotAllowedException: ", ex);
        return ExceptionResponse.builder()
                .code(ex.getMessage())
                .build();
    }

    @ExceptionHandler(CustomFeignException.class)
    public ResponseEntity<ExceptionResponse> handle(CustomFeignException ex) {
        log.error("CustomFeignException: ", ex);
        return ResponseEntity.status(ex.getStatusCode()).body(new ExceptionResponse(ex.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ExceptionResponse handle(Exception ex) {
        log.error("UnexpectedException: ", ex);
        return ExceptionResponse.builder()
                .code(UNEXPECTED_EXCEPTION.toString())
                .build();
    }
}