package com.library.service;

import com.library.model.Book;
import com.library.model.BookStatus;
import com.library.model.Reservation;
import com.library.model.ReservationStatus;
import com.library.model.User;
import com.library.repository.ReservationRepository;
import com.library.exception.BookNotFoundException;
import com.library.exception.ReservationNotFoundException;
import com.library.exception.UserNotFoundException;
import com.library.exception.LibraryException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing reservations in the library system.
 */
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BookService bookService;
    private final UserService userService;
    private final LoanService loanService;

    /**
     * Constructor for ReservationService.
     * 
     * @param reservationRepository The repository for reservation data
     * @param bookService The service for book operations
     * @param userService The service for user operations
     * @param loanService The service for loan operations
     */
    public ReservationService(ReservationRepository reservationRepository, BookService bookService, 
                             UserService userService, LoanService loanService) {
        this.reservationRepository = reservationRepository;
        this.bookService = bookService;
        this.userService = userService;
        this.loanService = loanService;
    }

    /**
     * Creates a new reservation for a user to reserve a book.
     * 
     * @param userId The ID of the user making the reservation
     * @param bookId The ID of the book being reserved
     * @return The newly created reservation
     * @throws UserNotFoundException If the user is not found
     * @throws BookNotFoundException If the book is not found
     * @throws LibraryException If the book is available (should be checked out directly) or the user cannot make reservations
     */
    public Reservation createReservation(String userId, String bookId) {
        User user = userService.getUserById(userId);
        Book book = bookService.getBookById(bookId);
        
        // Check if user can make reservations
        if (user.isAccountLocked() || user.getActiveReservations().size() >= user.getUserType().getMaxReservations()) {
            throw new LibraryException("User cannot make more reservations: account locked or maximum reservations reached");
        }
        
        // Check if book is already available (should be checked out directly)
        if (book.getStatus() == BookStatus.AVAILABLE) {
            throw new LibraryException("Book is available for checkout, no need to reserve");
        }
        
        // Check if user already has an active reservation for this book
        List<Reservation> userReservations = getActiveReservationsForUser(userId);
        boolean alreadyReserved = userReservations.stream()
                .anyMatch(r -> r.getBookId().equals(bookId) && r.isActive());
        
        if (alreadyReserved) {
            throw new LibraryException("User already has an active reservation for this book");
        }
        
        // Create the reservation
        Reservation reservation = new Reservation(userId, bookId);
        
        // Add reservation to user
        user.addReservation(reservation);
        
        return reservationRepository.save(reservation);
    }

    /**
     * Confirms a reservation, changing its status to CONFIRMED.
     * This typically happens when the book becomes available.
     * 
     * @param reservationId The ID of the reservation to confirm
     * @return The updated reservation
     * @throws ReservationNotFoundException If the reservation is not found
     * @throws LibraryException If the reservation cannot be confirmed
     */
    public Reservation confirmReservation(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found with ID: " + reservationId));
        
        boolean confirmed = reservation.confirmReservation();
        
        if (!confirmed) {
            throw new LibraryException("Reservation cannot be confirmed: not in PENDING status or already expired");
        }
        
        // Update book status to RESERVED
        Book book = bookService.getBookById(reservation.getBookId());
        book.setStatus(BookStatus.RESERVED);
        bookService.updateBookStatus(reservation.getBookId(), BookStatus.RESERVED);
        
        return reservationRepository.save(reservation);
    }

    /**
     * Fulfills a reservation, changing its status to FULFILLED.
     * This happens when the user checks out the reserved book.
     * 
     * @param reservationId The ID of the reservation to fulfill
     * @return The loan created for the reserved book
     * @throws ReservationNotFoundException If the reservation is not found
     * @throws LibraryException If the reservation cannot be fulfilled
     */
    public Reservation fulfillReservation(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found with ID: " + reservationId));
        
        boolean fulfilled = reservation.fulfillReservation();
        
        if (!fulfilled) {
            throw new LibraryException("Reservation cannot be fulfilled: not in PENDING or CONFIRMED status or already expired");
        }
        
        // Create a loan for the reserved book
        loanService.createLoan(reservation.getUserId(), reservation.getBookId());
        
        return reservationRepository.save(reservation);
    }

    /**
     * Cancels a reservation, changing its status to CANCELLED.
     * 
     * @param reservationId The ID of the reservation to cancel
     * @return The updated reservation
     * @throws ReservationNotFoundException If the reservation is not found
     * @throws LibraryException If the reservation cannot be cancelled
     */
    public Reservation cancelReservation(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found with ID: " + reservationId));
        
        boolean cancelled = reservation.cancelReservation();
        
        if (!cancelled) {
            throw new LibraryException("Reservation cannot be cancelled: not in PENDING or CONFIRMED status");
        }
        
        // If the book was reserved for this reservation, update its status back to AVAILABLE
        Book book = bookService.getBookById(reservation.getBookId());
        if (book.getStatus() == BookStatus.RESERVED) {
            book.setStatus(BookStatus.AVAILABLE);
            bookService.updateBookStatus(reservation.getBookId(), BookStatus.AVAILABLE);
        }
        
        return reservationRepository.save(reservation);
    }

    /**
     * Extends a reservation period.
     * 
     * @param reservationId The ID of the reservation to extend
     * @param additionalDays The number of additional days to extend the reservation
     * @return The updated reservation
     * @throws ReservationNotFoundException If the reservation is not found
     * @throws LibraryException If the reservation cannot be extended
     */
    public Reservation extendReservation(String reservationId, int additionalDays) {
        if (additionalDays <= 0) {
            throw new LibraryException("Additional days must be positive");
        }
        
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found with ID: " + reservationId));
        
        boolean extended = reservation.extendReservation(additionalDays);
        
        if (!extended) {
            throw new LibraryException("Reservation cannot be extended: not in PENDING or CONFIRMED status or already expired");
        }
        
        return reservationRepository.save(reservation);
    }

    /**
     * Gets a reservation by its ID.
     * 
     * @param reservationId The ID of the reservation to get
     * @return The reservation
     * @throws ReservationNotFoundException If the reservation is not found
     */
    public Reservation getReservationById(String reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found with ID: " + reservationId));
    }

    /**
     * Gets all active reservations for a user.
     * 
     * @param userId The ID of the user
     * @return A list of active reservations for the user
     * @throws UserNotFoundException If the user is not found
     */
    public List<Reservation> getActiveReservationsForUser(String userId) {
        userService.getUserById(userId); // Verify user exists
        List<Reservation> allReservations = reservationRepository.findByUserId(userId);
        return allReservations.stream()
                .filter(Reservation::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Gets all reservations for a user, including fulfilled, cancelled, and expired reservations.
     * 
     * @param userId The ID of the user
     * @return A list of all reservations for the user
     * @throws UserNotFoundException If the user is not found
     */
    public List<Reservation> getAllReservationsForUser(String userId) {
        userService.getUserById(userId); // Verify user exists
        return reservationRepository.findByUserId(userId);
    }

    /**
     * Gets all active reservations for a book.
     * 
     * @param bookId The ID of the book
     * @return A list of active reservations for the book
     * @throws BookNotFoundException If the book is not found
     */
    public List<Reservation> getActiveReservationsForBook(String bookId) {
        bookService.getBookById(bookId); // Verify book exists
        List<Reservation> allReservations = reservationRepository.findByBookId(bookId);
        return allReservations.stream()
                .filter(Reservation::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Gets all reservations for a book, including fulfilled, cancelled, and expired reservations.
     * 
     * @param bookId The ID of the book
     * @return A list of all reservations for the book
     * @throws BookNotFoundException If the book is not found
     */
    public List<Reservation> getAllReservationsForBook(String bookId) {
        bookService.getBookById(bookId); // Verify book exists
        return reservationRepository.findByBookId(bookId);
    }

    /**
     * Gets all active reservations in the library system.
     * 
     * @return A list of all active reservations
     */
    public List<Reservation> getAllActiveReservations() {
        List<Reservation> allReservations = reservationRepository.findAll();
        return allReservations.stream()
                .filter(Reservation::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Gets all reservations with a specific status.
     * 
     * @param status The status to filter by
     * @return A list of reservations with the specified status
     */
    public List<Reservation> getReservationsByStatus(ReservationStatus status) {
        return reservationRepository.findByStatus(status);
    }

    /**
     * Gets all expired reservations.
     * 
     * @return A list of all expired reservations
     */
    public List<Reservation> getExpiredReservations() {
        List<Reservation> allReservations = reservationRepository.findAll();
        return allReservations.stream()
                .filter(Reservation::isExpired)
                .collect(Collectors.toList());
    }

    /**
     * Processes expired reservations, updating their status to EXPIRED.
     * 
     * @return The number of reservations processed
     */
    public int processExpiredReservations() {
        List<Reservation> expiredReservations = getExpiredReservations();
        
        for (Reservation reservation : expiredReservations) {
            if (reservation.getStatus() == ReservationStatus.PENDING || reservation.getStatus() == ReservationStatus.CONFIRMED) {
                reservation.setStatus(ReservationStatus.EXPIRED);
                reservationRepository.save(reservation);
                
                // If the book was reserved for this reservation, update its status back to AVAILABLE
                Book book = bookService.getBookById(reservation.getBookId());
                if (book.getStatus() == BookStatus.RESERVED) {
                    book.setStatus(BookStatus.AVAILABLE);
                    bookService.updateBookStatus(reservation.getBookId(), BookStatus.AVAILABLE);
                }
            }
        }
        
        return expiredReservations.size();
    }

    /**
     * Gets all reservations created between two specific dates.
     * 
     * @param startDate The start date of the range
     * @param endDate The end date of the range
     * @return A list of reservations created between the specified dates
     */
    public List<Reservation> getReservationsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return reservationRepository.findByReservationDateBetween(startDate, endDate);
    }
}
