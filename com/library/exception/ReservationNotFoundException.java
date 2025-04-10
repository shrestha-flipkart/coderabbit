package com.library.exception;

/**
 * Exception thrown when a reservation is not found in the library system.
 */
public class ReservationNotFoundException extends LibraryException {
    /**
     * Constructs a new reservation not found exception with the specified detail message.
     * 
     * @param message The detail message
     */
    public ReservationNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new reservation not found exception with the specified detail message and cause.
     * 
     * @param message The detail message
     * @param cause The cause
     */
    public ReservationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
