package com.shawn.taskassistant.domain.exceptions.businessException;

public class usecaseException extends RuntimeException {
    
    public usecaseException(String message) {
        super(message);
    }

    public usecaseException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
}
