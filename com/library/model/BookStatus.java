package com.library.model;

/**
 * Represents the current status of a book in the library system.
 */
public enum BookStatus {
    AVAILABLE,     // Book is available for checkout
    CHECKED_OUT,   // Book is currently checked out
    RESERVED,      // Book is reserved for a user
    UNDER_REPAIR,  // Book is being repaired
    LOST,          // Book is reported lost
    ARCHIVED       // Book is archived and not in active circulation
}
