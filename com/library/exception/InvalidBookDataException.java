package com.library.exception;

/**
 * Exception thrown when invalid book data is provided.
 * This exception is used when book data validation fails.
 */
public class InvalidBookDataException extends RuntimeException {
    
    /**
     * Constructs a new InvalidBookDataException with the specified detail message.
     * 
     * @param message the detail message
     */
    public InvalidBookDataException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new InvalidBookDataException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public InvalidBookDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
