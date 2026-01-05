package com.shawn.taskassistant.domain.exceptions.technicalException;

public class BaseTechnicalException extends RuntimeException {
    public BaseTechnicalException(String message) {
        super(message);
    }

    public BaseTechnicalException(String message, Throwable cause) {
        super(message, cause);
    }
    
    
    
}
