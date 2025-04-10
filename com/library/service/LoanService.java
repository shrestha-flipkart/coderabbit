package com.library.service;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.Loan;
import com.library.model.User;
import com.library.repository.LoanRepository;
import com.library.exception.BookNotFoundException;
import com.library.exception.LoanNotFoundException;
import com.library.exception.UserNotFoundException;
import com.library.exception.LibraryException;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for managing loans in the library system.
 */
public class LoanService {
    private final LoanRepository loanRepository;
    private final BookService bookService;
    private final UserService userService;

    /**
     * Constructor for LoanService.
     * 
     * @param loanRepository The repository for loan data
     * @param bookService The service for book operations
     * @param userService The service for user operations
     */
    public LoanService(LoanRepository loanRepository, BookService bookService, UserService userService) {
        this.loanRepository = loanRepository;
        this.bookService = bookService;
        this.userService = userService;
    }

    /**
     * Creates a new loan for a user to borrow a book.
     * 
     * @param userId The ID of the user borrowing the book
     * @param bookId The ID of the book being borrowed
     * @return The newly created loan
     * @throws UserNotFoundException If the user is not found
     * @throws BookNotFoundException If the book is not found
     * @throws LibraryException If the book is not available or the user cannot borrow books
     */
    public Loan createLoan(String userId, String bookId) {
        User user = userService.getUserById(userId);
        Book book = bookService.getBookById(bookId);
        
        // Check if user can borrow books
        if (!user.canBorrowBooks()) {
            throw new LibraryException("User cannot borrow books: account locked or maximum loans reached");
        }
        
        // Check if book is available
        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new LibraryException("Book is not available for checkout");
        }
        
        // Create the loan
        Loan loan = new Loan(userId, bookId, user.getUserType().getLoanPeriodDays());
        
        // Update book status
        book.setStatus(BookStatus.CHECKED_OUT);
        bookService.updateBookStatus(bookId, BookStatus.CHECKED_OUT);
        
        // Add loan to user
        user.addLoan(loan);
        
        return loanRepository.save(loan);
    }

    /**
     * Returns a book that was borrowed.
     * 
     * @param loanId The ID of the loan to return
     * @return The fine amount if the book is returned late, 0.0 otherwise
     * @throws LoanNotFoundException If the loan is not found
     */
    public double returnBook(String loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found with ID: " + loanId));
        
        // Check if book is already returned
        if (!loan.isActive()) {
            throw new LibraryException("Book already returned");
        }
        
        // Return the book and calculate fine
        double fineAmount = loan.returnBook();
        
        // Update book status
        Book book = bookService.getBookById(loan.getBookId());
        book.setStatus(BookStatus.AVAILABLE);
        bookService.updateBookStatus(loan.getBookId(), BookStatus.AVAILABLE);
        
        // Update user's fine amount if necessary
        if (fineAmount > 0) {
            userService.addFineToUser(loan.getUserId(), fineAmount);
        }
        
        // Update loan in repository
        loanRepository.save(loan);
        
        return fineAmount;
    }

    /**
     * Renews a loan, extending the due date.
     * 
     * @param loanId The ID of the loan to renew
     * @return The updated loan
     * @throws LoanNotFoundException If the loan is not found
     * @throws LibraryException If the loan cannot be renewed
     */
    public Loan renewLoan(String loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found with ID: " + loanId));
        
        User user = userService.getUserById(loan.getUserId());
        int additionalDays = user.getUserType().getLoanPeriodDays();
        
        boolean renewed = loan.renewLoan(additionalDays);
        
        if (!renewed) {
            throw new LibraryException("Loan cannot be renewed: maximum renewals reached, book already returned, or loan is overdue");
        }
        
        return loanRepository.save(loan);
    }

    /**
     * Gets a loan by its ID.
     * 
     * @param loanId The ID of the loan to get
     * @return The loan
     * @throws LoanNotFoundException If the loan is not found
     */
    public Loan getLoanById(String loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new LoanNotFoundException("Loan not found with ID: " + loanId));
    }

    /**
     * Gets all active loans for a user.
     * 
     * @param userId The ID of the user
     * @return A list of active loans for the user
     * @throws UserNotFoundException If the user is not found
     */
    public List<Loan> getActiveLoansForUser(String userId) {
        userService.getUserById(userId); // Verify user exists
        return loanRepository.findByUserIdAndReturnDateIsNull(userId);
    }

    /**
     * Gets all loans for a user, including returned loans.
     * 
     * @param userId The ID of the user
     * @return A list of all loans for the user
     * @throws UserNotFoundException If the user is not found
     */
    public List<Loan> getAllLoansForUser(String userId) {
        userService.getUserById(userId); // Verify user exists
        return loanRepository.findByUserId(userId);
    }

    /**
     * Gets the loan history for a book.
     * 
     * @param bookId The ID of the book
     * @return A list of loans for the book
     * @throws BookNotFoundException If the book is not found
     */
    public List<Loan> getLoanHistoryForBook(String bookId) {
        bookService.getBookById(bookId); // Verify book exists
        return loanRepository.findByBookId(bookId);
    }

    /**
     * Gets all active loans in the library system.
     * 
     * @return A list of all active loans
     */
    public List<Loan> getAllActiveLoans() {
        return loanRepository.findByReturnDateIsNull();
    }

    /**
     * Gets all overdue loans in the library system.
     * 
     * @return A list of all overdue loans
     */
    public List<Loan> getOverdueLoans() {
        LocalDate today = LocalDate.now();
        return loanRepository.findByReturnDateIsNullAndDueDateBefore(today);
    }

    /**
     * Gets all loans that are due on a specific date.
     * 
     * @param dueDate The due date to filter by
     * @return A list of loans due on the specified date
     */
    public List<Loan> getLoansDueOn(LocalDate dueDate) {
        return loanRepository.findByDueDate(dueDate);
    }

    /**
     * Calculates the total fine amount for all overdue loans.
     * 
     * @return The total fine amount
     */
    public double calculateTotalFineAmount() {
        List<Loan> overdueLoans = getOverdueLoans();
        return overdueLoans.stream()
                .mapToDouble(Loan::calculateCurrentFine)
                .sum();
    }

    /**
     * Gets all loans created between two specific dates.
     * 
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of loans created between the specified dates
     */
    public List<Loan> getLoansBetweenDates(LocalDate startDate, LocalDate endDate) {
        return loanRepository.findByLoanDateBetween(startDate, endDate);
    }

    /**
     * Gets all loans for a specific book that are currently active.
     * 
     * @param bookId The ID of the book
     * @return The active loan for the book, or null if the book is not currently on loan
     * @throws BookNotFoundException If the book is not found
     */
    public Loan getActiveLoanForBook(String bookId) {
        bookService.getBookById(bookId); // Verify book exists
        List<Loan> activeLoans = loanRepository.findByBookIdAndReturnDateIsNull(bookId);
        return activeLoans.isEmpty() ? null : activeLoans.get(0);
    }
}
