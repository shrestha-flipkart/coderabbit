package com.library.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Represents a loan of a book to a user in the library system.
 */
public class Loan {
    private final String id;
    private final String userId;
    private final String bookId;
    private final LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private double fineAmount;
    private boolean renewed;
    private int renewalCount;
    private static final int MAX_RENEWALS = 2;
    private static final double FINE_RATE_PER_DAY = 0.50;

    /**
     * Constructor for creating a new loan.
     * 
     * @param userId The ID of the user borrowing the book
     * @param bookId The ID of the book being borrowed
     * @param loanPeriodDays The number of days for which the book is loaned
     */
    public Loan(String userId, String bookId, int loanPeriodDays) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.bookId = bookId;
        this.loanDate = LocalDate.now();
        this.dueDate = loanDate.plusDays(loanPeriodDays);
        this.returnDate = null;
        this.fineAmount = 0.0;
        this.renewed = false;
        this.renewalCount = 0;
    }

    /**
     * Renews the loan, extending the due date.
     * 
     * @param additionalDays The number of additional days to extend the loan
     * @return true if the loan was successfully renewed, false otherwise
     */
    public boolean renewLoan(int additionalDays) {
        if (returnDate != null) {
            return false; // Cannot renew a returned loan
        }
        
        if (renewalCount >= MAX_RENEWALS) {
            return false; // Maximum renewals reached
        }
        
        if (isOverdue()) {
            return false; // Cannot renew an overdue loan
        }
        
        this.dueDate = this.dueDate.plusDays(additionalDays);
        this.renewed = true;
        this.renewalCount++;
        return true;
    }

    /**
     * Returns the book, setting the return date.
     * 
     * @return The fine amount if the book is returned late, 0.0 otherwise
     */
    public double returnBook() {
        if (returnDate != null) {
            return 0.0; // Book already returned
        }
        
        this.returnDate = LocalDate.now();
        
        if (isOverdue()) {
            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
            this.fineAmount = daysLate * FINE_RATE_PER_DAY;
            return this.fineAmount;
        }
        
        return 0.0;
    }

    /**
     * Checks if the loan is overdue.
     * 
     * @return true if the loan is overdue, false otherwise
     */
    public boolean isOverdue() {
        LocalDate today = LocalDate.now();
        return returnDate == null && today.isAfter(dueDate);
    }

    /**
     * Calculates the current fine amount for an overdue loan.
     * 
     * @return The current fine amount
     */
    public double calculateCurrentFine() {
        if (returnDate != null) {
            return fineAmount; // Fine is already calculated and fixed
        }
        
        if (isOverdue()) {
            LocalDate today = LocalDate.now();
            long daysLate = ChronoUnit.DAYS.between(dueDate, today);
            return daysLate * FINE_RATE_PER_DAY;
        }
        
        return 0.0;
    }

    /**
     * Checks if the loan is active (not returned).
     * 
     * @return true if the loan is active, false otherwise
     */
    public boolean isActive() {
        return returnDate == null;
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

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public boolean isRenewed() {
        return renewed;
    }

    public int getRenewalCount() {
        return renewalCount;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", loanDate=" + loanDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", fineAmount=" + fineAmount +
                ", renewed=" + renewed +
                ", renewalCount=" + renewalCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Loan loan = (Loan) o;

        return id.equals(loan.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
