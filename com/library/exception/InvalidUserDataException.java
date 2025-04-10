package com.library.exception;

/**
 * Exception thrown when invalid user data is provided.
 */
public class InvalidUserDataException extends RuntimeException {
    
    /**
     * Constructs a new InvalidUserDataException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidUserDataException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new InvalidUserDataException with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public InvalidUserDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
