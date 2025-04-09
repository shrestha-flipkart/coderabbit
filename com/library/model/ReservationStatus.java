package com.library.model;

/**
 * Represents the status of a reservation in the library system.
 */
public enum ReservationStatus {
    PENDING,    // Reservation is pending confirmation
    CONFIRMED,  // Reservation is confirmed, book is being held
    FULFILLED,  // Reservation is fulfilled (book has been checked out)
    CANCELLED,  // Reservation has been cancelled
    EXPIRED     // Reservation has expired
}
