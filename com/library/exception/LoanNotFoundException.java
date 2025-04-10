package com.library.exception;

/**
 * Exception thrown when a loan cannot be found in the system.
 */
public class LoanNotFoundException extends RuntimeException {
    
    /**
     * Constructs a new LoanNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public LoanNotFoundException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new LoanNotFoundException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public LoanNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
