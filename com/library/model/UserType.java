package com.library.model;

/**
 * Represents the type of user in the library system.
 * Different user types have different privileges.
 */
public enum UserType {
    STUDENT(3, 2, 14),
    FACULTY(5, 3, 30),
    STAFF(4, 2, 21),
    RESEARCHER(7, 5, 60),
    ADMIN(10, 5, 30),
    GUEST(1, 0, 7);

    private final int maxLoans;
    private final int maxReservations;
    private final int loanPeriodDays;

    /**
     * Constructor for UserType.
     * 
     * @param maxLoans The maximum number of books a user of this type can borrow
     * @param maxReservations The maximum number of books a user of this type can reserve
     * @param loanPeriodDays The standard loan period in days for this user type
     */
    UserType(int maxLoans, int maxReservations, int loanPeriodDays) {
        this.maxLoans = maxLoans;
        this.maxReservations = maxReservations;
        this.loanPeriodDays = loanPeriodDays;
    }

    public int getMaxLoans() {
        return maxLoans;
    }

    public int getMaxReservations() {
        return maxReservations;
    }

    public int getLoanPeriodDays() {
        return loanPeriodDays;
    }
}
