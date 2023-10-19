package az.ingress.exception;


import lombok.Getter;
import lombok.experimental.FieldDefaults;

import static lombok.AccessLevel.PRIVATE;

@Getter
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class CustomFeignException extends RuntimeException {
    Integer statusCode;

    public CustomFeignException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}