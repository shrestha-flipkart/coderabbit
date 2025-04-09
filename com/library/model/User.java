package com.library.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a user in the library system.
 * Contains all the information about a library member.
 */
public class User {
    private final String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate registrationDate;
    private UserType userType;
    private List<Loan> activeLoans;
    private List<Reservation> activeReservations;
    private boolean accountLocked;
    private double fineAmount;

    /**
     * Constructor for creating a new user.
     * 
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param email The email of the user
     * @param phoneNumber The phone number of the user
     * @param userType The type of user (STUDENT, FACULTY, etc.)
     */
    public User(String firstName, String lastName, String email, String phoneNumber, UserType userType) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.registrationDate = LocalDate.now();
        this.userType = userType;
        this.activeLoans = new ArrayList<>();
        this.activeReservations = new ArrayList<>();
        this.accountLocked = false;
        this.fineAmount = 0.0;
    }

    /**
     * Adds a loan to the user's active loans.
     * 
     * @param loan The loan to add
     */
    public void addLoan(Loan loan) {
        if (canBorrowBooks()) {
            activeLoans.add(loan);
        } else {
            throw new IllegalStateException("User cannot borrow more books: account locked or maximum loans reached");
        }
    }

    /**
     * Removes a loan from the user's active loans.
     * 
     * @param loan The loan to remove
     * @return true if the loan was successfully removed, false otherwise
     */
    public boolean removeLoan(Loan loan) {
        return activeLoans.remove(loan);
    }

    /**
     * Adds a reservation to the user's active reservations.
     * 
     * @param reservation The reservation to add
     */
    public void addReservation(Reservation reservation) {
        if (!accountLocked && activeReservations.size() < userType.getMaxReservations()) {
            activeReservations.add(reservation);
        } else {
            throw new IllegalStateException("User cannot make more reservations: account locked or maximum reservations reached");
        }
    }

    /**
     * Removes a reservation from the user's active reservations.
     * 
     * @param reservation The reservation to remove
     * @return true if the reservation was successfully removed, false otherwise
     */
    public boolean removeReservation(Reservation reservation) {
        return activeReservations.remove(reservation);
    }

    /**
     * Checks if the user can borrow books.
     * 
     * @return true if the user can borrow books, false otherwise
     */
    public boolean canBorrowBooks() {
        return !accountLocked && activeLoans.size() < userType.getMaxLoans() && fineAmount < 10.0;
    }

    /**
     * Adds a fine to the user's account.
     * 
     * @param amount The amount to add to the fine
     */
    public void addFine(double amount) {
        if (amount > 0) {
            this.fineAmount += amount;
            if (this.fineAmount >= 50.0) {
                this.accountLocked = true;
            }
        }
    }

    /**
     * Pays a fine from the user's account.
     * 
     * @param amount The amount to pay
     * @return true if the payment was successful, false otherwise
     */
    public boolean payFine(double amount) {
        if (amount > 0 && amount <= this.fineAmount) {
            this.fineAmount -= amount;
            if (this.fineAmount < 50.0 && this.accountLocked) {
                this.accountLocked = false;
            }
            return true;
        }
        return false;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public List<Loan> getActiveLoans() {
        return new ArrayList<>(activeLoans);
    }

    public List<Reservation> getActiveReservations() {
        return new ArrayList<>(activeReservations);
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", registrationDate=" + registrationDate +
                ", userType=" + userType +
                ", activeLoans=" + activeLoans.size() +
                ", activeReservations=" + activeReservations.size() +
                ", accountLocked=" + accountLocked +
                ", fineAmount=" + fineAmount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
