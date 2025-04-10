package com.library.exception;

/**
 * Base exception class for all library-related exceptions.
 */
public class LibraryException extends RuntimeException {
    /**
     * Constructs a new library exception with the specified detail message.
     * 
     * @param message The detail message
     */
    public LibraryException(String message) {
        super(message);
    }

    /**
     * Constructs a new library exception with the specified detail message and cause.
     * 
     * @param message The detail message
     * @param cause The cause
     */
    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }
}
