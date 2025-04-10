package com.library.repository;

import com.library.model.Book;
import com.library.model.BookCategory;
import com.library.model.BookStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Book entities.
 */
public interface BookRepository {
    /**
     * Saves a book to the repository.
     * 
     * @param book The book to save
     * @return The saved book
     */
    Book save(Book book);
    
    /**
     * Finds a book by its ID.
     * 
     * @param id The ID of the book to find
     * @return An Optional containing the book if found, or empty if not found
     */
    Optional<Book> findById(String id);
    
    /**
     * Checks if a book with the given ID exists.
     * 
     * @param id The ID to check
     * @return true if a book with the ID exists, false otherwise
     */
    boolean existsById(String id);
    
    /**
     * Deletes a book by its ID.
     * 
     * @param id The ID of the book to delete
     */
    void deleteById(String id);
    
    /**
     * Finds all books in the repository.
     * 
     * @return A list of all books
     */
    List<Book> findAll();
    
    /**
     * Finds books by their status.
     * 
     * @param status The status to filter by
     * @return A list of books with the specified status
     */
    List<Book> findByStatus(BookStatus status);
    
    /**
     * Finds books by their category.
     * 
     * @param category The category to filter by
     * @return A list of books in the specified category
     */
    List<Book> findByCategory(BookCategory category);
    
    /**
     * Finds books with titles containing the given keyword (case-insensitive).
     * 
     * @param titleKeyword The keyword to search for in titles
     * @return A list of books with titles containing the keyword
     */
    List<Book> findByTitleContainingIgnoreCase(String titleKeyword);
    
    /**
     * Finds books with authors containing the given keyword (case-insensitive).
     * 
     * @param authorKeyword The keyword to search for in authors
     * @return A list of books with authors containing the keyword
     */
    List<Book> findByAuthorContainingIgnoreCase(String authorKeyword);
    
    /**
     * Finds books published after the given date.
     * 
     * @param date The date to filter by
     * @return A list of books published after the specified date
     */
    List<Book> findByPublishDateAfter(LocalDate date);
    
    /**
     * Finds books published before the given date.
     * 
     * @param date The date to filter by
     * @return A list of books published before the specified date
     */
    List<Book> findByPublishDateBefore(LocalDate date);
    
    /**
     * Finds books published between the given dates.
     * 
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of books published between the specified dates
     */
    List<Book> findByPublishDateBetween(LocalDate startDate, LocalDate endDate);
}
