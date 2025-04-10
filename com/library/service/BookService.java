package com.library.service;

import com.library.model.Book;
import com.library.model.BookCategory;
import com.library.model.BookStatus;
import com.library.repository.BookRepository;
import com.library.exception.BookNotFoundException;
import com.library.exception.InvalidBookDataException;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for managing books in the library system.
 */
public class BookService {
    private final BookRepository bookRepository;

    /**
     * Constructor for BookService.
     * 
     * @param bookRepository The repository for book data
     */
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Adds a new book to the library.
     * 
     * @param title The title of the book
     * @param author The author of the book
     * @param isbn The ISBN of the book
     * @param publishDate The publication date of the book
     * @param category The category of the book
     * @return The newly created book
     * @throws InvalidBookDataException If the book data is invalid
     */
    public Book addBook(String title, String author, String isbn, LocalDate publishDate, BookCategory category) {
        validateBookData(title, author, isbn, publishDate);
        
        Book book = new Book(title, author, isbn, publishDate, category);
        return bookRepository.save(book);
    }

    /**
     * Updates an existing book in the library.
     * 
     * @param bookId The ID of the book to update
     * @param title The new title of the book
     * @param author The new author of the book
     * @param isbn The new ISBN of the book
     * @param publishDate The new publication date of the book
     * @param category The new category of the book
     * @return The updated book
     * @throws BookNotFoundException If the book is not found
     * @throws InvalidBookDataException If the book data is invalid
     */
    public Book updateBook(String bookId, String title, String author, String isbn, LocalDate publishDate, BookCategory category) {
        validateBookData(title, author, isbn, publishDate);
        
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));
        
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPublishDate(publishDate);
        book.setCategory(category);
        
        return bookRepository.save(book);
    }

    /**
     * Updates the status of a book.
     * 
     * @param bookId The ID of the book to update
     * @param status The new status of the book
     * @return The updated book
     * @throws BookNotFoundException If the book is not found
     */
    public Book updateBookStatus(String bookId, BookStatus status) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));
        
        book.setStatus(status);
        return bookRepository.save(book);
    }

    /**
     * Deletes a book from the library.
     * 
     * @param bookId The ID of the book to delete
     * @throws BookNotFoundException If the book is not found
     */
    public void deleteBook(String bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new BookNotFoundException("Book not found with ID: " + bookId);
        }
        
        bookRepository.deleteById(bookId);
    }

    /**
     * Gets a book by its ID.
     * 
     * @param bookId The ID of the book to get
     * @return The book
     * @throws BookNotFoundException If the book is not found
     */
    public Book getBookById(String bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));
    }

    /**
     * Gets all books in the library.
     * 
     * @return A list of all books
     */
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Gets all books with a specific status.
     * 
     * @param status The status to filter by
     * @return A list of books with the specified status
     */
    public List<Book> getBooksByStatus(BookStatus status) {
        return bookRepository.findByStatus(status);
    }

    /**
     * Gets all books in a specific category.
     * 
     * @param category The category to filter by
     * @return A list of books in the specified category
     */
    public List<Book> getBooksByCategory(BookCategory category) {
        return bookRepository.findByCategory(category);
    }

    /**
     * Searches for books by title.
     * 
     * @param titleKeyword The keyword to search for in book titles
     * @return A list of books with titles containing the keyword
     */
    public List<Book> searchBooksByTitle(String titleKeyword) {
        return bookRepository.findByTitleContainingIgnoreCase(titleKeyword);
    }

    /**
     * Searches for books by author.
     * 
     * @param authorKeyword The keyword to search for in book authors
     * @return A list of books with authors containing the keyword
     */
    public List<Book> searchBooksByAuthor(String authorKeyword) {
        return bookRepository.findByAuthorContainingIgnoreCase(authorKeyword);
    }

    /**
     * Gets all available books (books with status AVAILABLE).
     * 
     * @return A list of available books
     */
    public List<Book> getAvailableBooks() {
        return bookRepository.findByStatus(BookStatus.AVAILABLE);
    }

    /**
     * Gets all books published after a specific date.
     * 
     * @param date The date to filter by
     * @return A list of books published after the specified date
     */
    public List<Book> getBooksPublishedAfter(LocalDate date) {
        return bookRepository.findByPublishDateAfter(date);
    }

    /**
     * Gets all books published before a specific date.
     * 
     * @param date The date to filter by
     * @return A list of books published before the specified date
     */
    public List<Book> getBooksPublishedBefore(LocalDate date) {
        return bookRepository.findByPublishDateBefore(date);
    }

    /**
     * Gets all books published between two specific dates.
     * 
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of books published between the specified dates
     */
    public List<Book> getBooksPublishedBetween(LocalDate startDate, LocalDate endDate) {
        return bookRepository.findByPublishDateBetween(startDate, endDate);
    }

    /**
     * Validates book data.
     * 
     * @param title The title of the book
     * @param author The author of the book
     * @param isbn The ISBN of the book
     * @param publishDate The publication date of the book
     * @throws InvalidBookDataException If the book data is invalid
     */
    private void validateBookData(String title, String author, String isbn, LocalDate publishDate) {
        if (title == null || title.trim().isEmpty()) {
            throw new InvalidBookDataException("Book title cannot be empty");
        }
        
        if (author == null || author.trim().isEmpty()) {
            throw new InvalidBookDataException("Book author cannot be empty");
        }
        
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new InvalidBookDataException("Book ISBN cannot be empty");
        }
        
        if (publishDate == null) {
            throw new InvalidBookDataException("Book publication date cannot be null");
        }
        
        if (publishDate.isAfter(LocalDate.now())) {
            throw new InvalidBookDataException("Book publication date cannot be in the future");
        }
        
        // Validate ISBN format (simplified)
        if (!isbn.matches("^\\d{10}$|^\\d{13}$")) {
            throw new InvalidBookDataException("Invalid ISBN format. Must be 10 or 13 digits");
        }
    }
}
