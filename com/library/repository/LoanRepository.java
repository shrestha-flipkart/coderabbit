package com.library.repository;

import com.library.model.Loan;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Loan entities.
 */
public interface LoanRepository {
    /**
     * Saves a loan to the repository.
     * 
     * @param loan The loan to save
     * @return The saved loan
     */
    Loan save(Loan loan);
    
    /**
     * Finds a loan by its ID.
     * 
     * @param id The ID of the loan to find
     * @return An Optional containing the loan if found, or empty if not found
     */
    Optional<Loan> findById(String id);
    
    /**
     * Checks if a loan with the given ID exists.
     * 
     * @param id The ID to check
     * @return true if a loan with the ID exists, false otherwise
     */
    boolean existsById(String id);
    
    /**
     * Deletes a loan by its ID.
     * 
     * @param id The ID of the loan to delete
     */
    void deleteById(String id);
    
    /**
     * Finds all loans in the repository.
     * 
     * @return A list of all loans
     */
    List<Loan> findAll();
    
    /**
     * Finds loans by user ID.
     * 
     * @param userId The user ID to filter by
     * @return A list of loans for the specified user
     */
    List<Loan> findByUserId(String userId);
    
    /**
     * Finds loans by book ID.
     * 
     * @param bookId The book ID to filter by
     * @return A list of loans for the specified book
     */
    List<Loan> findByBookId(String bookId);
    
    /**
     * Finds active loans (not returned) by user ID.
     * 
     * @param userId The user ID to filter by
     * @return A list of active loans for the specified user
     */
    List<Loan> findByUserIdAndReturnDateIsNull(String userId);
    
    /**
     * Finds active loans (not returned) by book ID.
     * 
     * @param bookId The book ID to filter by
     * @return A list of active loans for the specified book
     */
    List<Loan> findByBookIdAndReturnDateIsNull(String bookId);
    
    /**
     * Finds all active loans (not returned).
     * 
     * @return A list of all active loans
     */
    List<Loan> findByReturnDateIsNull();
    
    /**
     * Finds overdue loans (not returned and due date before the current date).
     * 
     * @param date The date to compare with the due date
     * @return A list of overdue loans
     */
    List<Loan> findByReturnDateIsNullAndDueDateBefore(LocalDate date);
    
    /**
     * Finds loans by due date.
     * 
     * @param dueDate The due date to filter by
     * @return A list of loans with the specified due date
     */
    List<Loan> findByDueDate(LocalDate dueDate);
    
    /**
     * Finds loans created between two specific dates.
     * 
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of loans created between the specified dates
     */
    List<Loan> findByLoanDateBetween(LocalDate startDate, LocalDate endDate);
}
