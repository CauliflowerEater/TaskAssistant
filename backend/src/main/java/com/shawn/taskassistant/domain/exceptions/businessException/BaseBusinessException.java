package com.shawn.taskassistant.domain.exceptions.businessException;

public class BaseBusinessException extends RuntimeException {
    
    public BaseBusinessException(String message) {
        super(message);
    }

    public BaseBusinessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
}
