package com.library.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Represents a reservation of a book by a user in the library system.
 */
public class Reservation {
    private final String id;
    private final String userId;
    private final String bookId;
    private final LocalDate reservationDate;
    private LocalDate expirationDate;
    private ReservationStatus status;
    private static final int DEFAULT_RESERVATION_DAYS = 3;

    /**
     * Constructor for creating a new reservation.
     * 
     * @param userId The ID of the user making the reservation
     * @param bookId The ID of the book being reserved
     */
    public Reservation(String userId, String bookId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.bookId = bookId;
        this.reservationDate = LocalDate.now();
        this.expirationDate = reservationDate.plusDays(DEFAULT_RESERVATION_DAYS);
        this.status = ReservationStatus.PENDING;
    }

    /**
     * Constructor for creating a new reservation with a custom expiration period.
     * 
     * @param userId The ID of the user making the reservation
     * @param bookId The ID of the book being reserved
     * @param reservationDays The number of days for which the reservation is valid
     */
    public Reservation(String userId, String bookId, int reservationDays) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.bookId = bookId;
        this.reservationDate = LocalDate.now();
        this.expirationDate = reservationDate.plusDays(reservationDays);
        this.status = ReservationStatus.PENDING;
    }

    /**
     * Confirms the reservation, changing its status to CONFIRMED.
     * 
     * @return true if the reservation was successfully confirmed, false otherwise
     */
    public boolean confirmReservation() {
        if (status == ReservationStatus.PENDING && !isExpired()) {
            this.status = ReservationStatus.CONFIRMED;
            return true;
        }
        return false;
    }

    /**
     * Fulfills the reservation, changing its status to FULFILLED.
     * This happens when the user checks out the reserved book.
     * 
     * @return true if the reservation was successfully fulfilled, false otherwise
     */
    public boolean fulfillReservation() {
        if ((status == ReservationStatus.PENDING || status == ReservationStatus.CONFIRMED) && !isExpired()) {
            this.status = ReservationStatus.FULFILLED;
            return true;
        }
        return false;
    }

    /**
     * Cancels the reservation, changing its status to CANCELLED.
     * 
     * @return true if the reservation was successfully cancelled, false otherwise
     */
    public boolean cancelReservation() {
        if (status == ReservationStatus.PENDING || status == ReservationStatus.CONFIRMED) {
            this.status = ReservationStatus.CANCELLED;
            return true;
        }
        return false;
    }

    /**
     * Extends the reservation period.
     * 
     * @param additionalDays The number of additional days to extend the reservation
     * @return true if the reservation was successfully extended, false otherwise
     */
    public boolean extendReservation(int additionalDays) {
        if ((status == ReservationStatus.PENDING || status == ReservationStatus.CONFIRMED) && !isExpired()) {
            this.expirationDate = this.expirationDate.plusDays(additionalDays);
            return true;
        }
        return false;
    }

    /**
     * Checks if the reservation is expired.
     * 
     * @return true if the reservation is expired, false otherwise
     */
    public boolean isExpired() {
        LocalDate today = LocalDate.now();
        return today.isAfter(expirationDate);
    }

    /**
     * Calculates the number of days until the reservation expires.
     * 
     * @return The number of days until expiration, or 0 if already expired
     */
    public long daysUntilExpiration() {
        LocalDate today = LocalDate.now();
        if (today.isAfter(expirationDate)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(today, expirationDate);
    }

    /**
     * Checks if the reservation is active (not fulfilled, cancelled, or expired).
     * 
     * @return true if the reservation is active, false otherwise
     */
    public boolean isActive() {
        return (status == ReservationStatus.PENDING || status == ReservationStatus.CONFIRMED) && !isExpired();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getBookId() {
        return bookId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }
    
    /**
     * Sets the status of the reservation.
     * 
     * @param status The new status to set
     */
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", reservationDate=" + reservationDate +
                ", expirationDate=" + expirationDate +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reservation that = (Reservation) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
