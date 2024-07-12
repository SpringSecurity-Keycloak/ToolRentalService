package io.rentalapp.common;

/**
 * Exception that should be thrown by any class that has fails business related rules
 */
public class ValidationException extends RuntimeException{
    public ValidationException(String message){
        super(message);
    }

}
