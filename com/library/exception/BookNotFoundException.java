package com.library.exception;

/**
 * Exception thrown when a book is not found in the library system.
 */
public class BookNotFoundException extends LibraryException {
    /**
     * Constructs a new book not found exception with the specified detail message.
     * 
     * @param message The detail message
     */
    public BookNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new book not found exception with the specified detail message and cause.
     * 
     * @param message The detail message
     * @param cause The cause
     */
    public BookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
