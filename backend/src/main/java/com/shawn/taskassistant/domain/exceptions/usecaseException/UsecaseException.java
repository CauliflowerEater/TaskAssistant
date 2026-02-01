package com.shawn.taskassistant.domain.exceptions.usecaseException;

public class UsecaseException extends RuntimeException {
    
    public UsecaseException(String message) {
        super(message);
    }

    public UsecaseException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
}
