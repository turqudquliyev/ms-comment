package az.ingress.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static az.ingress.model.enums.ExceptionMessage.UNEXPECTED_EXCEPTION;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(METHOD_NOT_ALLOWED)
    public ExceptionResponse handle(HttpRequestMethodNotSupportedException ex) {
        log.error("MethodNotAllowedException: ", ex);
        return new ExceptionResponse(ex.getMessage());
    }

    @ExceptionHandler(CustomFeignException.class)
    public ResponseEntity<ExceptionResponse> handle(CustomFeignException ex) {
        log.error("CustomFeignException: ", ex);
        return ResponseEntity.status(ex.getStatusCode()).body(new ExceptionResponse(ex.getMessage()));
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ExceptionResponse handle(NotFoundException ex) {
        log.error("NotFoundException: ", ex);
        return new ExceptionResponse(ex.getMessage());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ExceptionResponse handle(Exception ex) {
        log.error("Unexpected Exception: ", ex);
        return new ExceptionResponse(UNEXPECTED_EXCEPTION.toString());
    }
}