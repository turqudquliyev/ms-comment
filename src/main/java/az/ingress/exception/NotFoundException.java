package az.ingress.exception;

import az.ingress.model.enums.ExceptionMessage;

public class NotFoundException extends RuntimeException {
    public NotFoundException(ExceptionMessage message) {
        super(message.toString());
    }
}