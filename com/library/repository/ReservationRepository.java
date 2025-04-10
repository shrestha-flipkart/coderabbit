package com.library.repository;

import com.library.model.Reservation;
import com.library.model.ReservationStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Reservation entities.
 */
public interface ReservationRepository {
    /**
     * Saves a reservation to the repository.
     * 
     * @param reservation The reservation to save
     * @return The saved reservation
     */
    Reservation save(Reservation reservation);
    
    /**
     * Finds a reservation by its ID.
     * 
     * @param id The ID of the reservation to find
     * @return An Optional containing the reservation if found, or empty if not found
     */
    Optional<Reservation> findById(String id);
    
    /**
     * Checks if a reservation with the given ID exists.
     * 
     * @param id The ID to check
     * @return true if a reservation with the ID exists, false otherwise
     */
    boolean existsById(String id);
    
    /**
     * Deletes a reservation by its ID.
     * 
     * @param id The ID of the reservation to delete
     */
    void deleteById(String id);
    
    /**
     * Finds all reservations in the repository.
     * 
     * @return A list of all reservations
     */
    List<Reservation> findAll();
    
    /**
     * Finds reservations by user ID.
     * 
     * @param userId The user ID to filter by
     * @return A list of reservations for the specified user
     */
    List<Reservation> findByUserId(String userId);
    
    /**
     * Finds reservations by book ID.
     * 
     * @param bookId The book ID to filter by
     * @return A list of reservations for the specified book
     */
    List<Reservation> findByBookId(String bookId);
    
    /**
     * Finds reservations by status.
     * 
     * @param status The status to filter by
     * @return A list of reservations with the specified status
     */
    List<Reservation> findByStatus(ReservationStatus status);
    
    /**
     * Finds reservations created between two specific dates.
     * 
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of reservations created between the specified dates
     */
    List<Reservation> findByReservationDateBetween(LocalDate startDate, LocalDate endDate);
}
