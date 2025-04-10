package com.library.service;

import com.library.model.User;
import com.library.model.UserType;
import com.library.repository.UserRepository;
import com.library.exception.UserNotFoundException;
import com.library.exception.InvalidUserDataException;

import java.util.List;

/**
 * Service class for managing users in the library system.
 */
public class UserService {
    private final UserRepository userRepository;

    /**
     * Constructor for UserService.
     * 
     * @param userRepository The repository for user data
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Registers a new user in the library system.
     * 
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param email The email of the user
     * @param phoneNumber The phone number of the user
     * @param userType The type of user
     * @return The newly registered user
     * @throws InvalidUserDataException If the user data is invalid
     */
    public User registerUser(String firstName, String lastName, String email, String phoneNumber, UserType userType) {
        validateUserData(firstName, lastName, email, phoneNumber);
        
        // Check if user with the same email already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new InvalidUserDataException("User with email " + email + " already exists");
        }
        
        User user = new User(firstName, lastName, email, phoneNumber, userType);
        return userRepository.save(user);
    }

    /**
     * Updates an existing user in the library system.
     * 
     * @param userId The ID of the user to update
     * @param firstName The new first name of the user
     * @param lastName The new last name of the user
     * @param email The new email of the user
     * @param phoneNumber The new phone number of the user
     * @param userType The new type of user
     * @return The updated user
     * @throws UserNotFoundException If the user is not found
     * @throws InvalidUserDataException If the user data is invalid
     */
    public User updateUser(String userId, String firstName, String lastName, String email, String phoneNumber, UserType userType) {
        validateUserData(firstName, lastName, email, phoneNumber);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        
        // Check if email is being changed and if the new email is already in use
        if (!user.getEmail().equals(email) && userRepository.findByEmail(email).isPresent()) {
            throw new InvalidUserDataException("User with email " + email + " already exists");
        }
        
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setUserType(userType);
        
        return userRepository.save(user);
    }

    /**
     * Deletes a user from the library system.
     * 
     * @param userId The ID of the user to delete
     * @throws UserNotFoundException If the user is not found
     */
    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
        
        userRepository.deleteById(userId);
    }

    /**
     * Gets a user by their ID.
     * 
     * @param userId The ID of the user to get
     * @return The user
     * @throws UserNotFoundException If the user is not found
     */
    public User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
    }

    /**
     * Gets a user by their email.
     * 
     * @param email The email of the user to get
     * @return The user
     * @throws UserNotFoundException If the user is not found
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    /**
     * Gets all users in the library system.
     * 
     * @return A list of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Gets all users of a specific type.
     * 
     * @param userType The type of users to get
     * @return A list of users of the specified type
     */
    public List<User> getUsersByType(UserType userType) {
        return userRepository.findByUserType(userType);
    }

    /**
     * Locks a user's account.
     * 
     * @param userId The ID of the user whose account should be locked
     * @return The updated user
     * @throws UserNotFoundException If the user is not found
     */
    public User lockUserAccount(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        
        user.setAccountLocked(true);
        return userRepository.save(user);
    }

    /**
     * Unlocks a user's account.
     * 
     * @param userId The ID of the user whose account should be unlocked
     * @return The updated user
     * @throws UserNotFoundException If the user is not found
     */
    public User unlockUserAccount(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        
        user.setAccountLocked(false);
        return userRepository.save(user);
    }

    /**
     * Adds a fine to a user's account.
     * 
     * @param userId The ID of the user to fine
     * @param amount The amount of the fine
     * @return The updated user
     * @throws UserNotFoundException If the user is not found
     * @throws InvalidUserDataException If the fine amount is invalid
     */
    public User addFineToUser(String userId, double amount) {
        if (amount <= 0) {
            throw new InvalidUserDataException("Fine amount must be positive");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        
        user.addFine(amount);
        return userRepository.save(user);
    }

    /**
     * Processes a fine payment for a user.
     * 
     * @param userId The ID of the user making the payment
     * @param amount The amount of the payment
     * @return The updated user
     * @throws UserNotFoundException If the user is not found
     * @throws InvalidUserDataException If the payment amount is invalid
     */
    public User processFinePayment(String userId, double amount) {
        if (amount <= 0) {
            throw new InvalidUserDataException("Payment amount must be positive");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
        
        if (amount > user.getFineAmount()) {
            throw new InvalidUserDataException("Payment amount exceeds outstanding fine");
        }
        
        user.payFine(amount);
        return userRepository.save(user);
    }

    /**
     * Searches for users by name.
     * 
     * @param nameKeyword The keyword to search for in user names
     * @return A list of users with names containing the keyword
     */
    public List<User> searchUsersByName(String nameKeyword) {
        return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(nameKeyword, nameKeyword);
    }

    /**
     * Gets all users with locked accounts.
     * 
     * @return A list of users with locked accounts
     */
    public List<User> getUsersWithLockedAccounts() {
        return userRepository.findByAccountLocked(true);
    }

    /**
     * Gets all users with outstanding fines.
     * 
     * @return A list of users with outstanding fines
     */
    public List<User> getUsersWithFines() {
        return userRepository.findByFineAmountGreaterThan(0.0);
    }

    /**
     * Validates user data.
     * 
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param email The email of the user
     * @param phoneNumber The phone number of the user
     * @throws InvalidUserDataException If the user data is invalid
     */
    private void validateUserData(String firstName, String lastName, String email, String phoneNumber) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new InvalidUserDataException("First name cannot be empty");
        }
        
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new InvalidUserDataException("Last name cannot be empty");
        }
        
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidUserDataException("Email cannot be empty");
        }
        
        // Validate email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new InvalidUserDataException("Invalid email format");
        }
        
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new InvalidUserDataException("Phone number cannot be empty");
        }
        
        // Validate phone number format (simplified)
        if (!phoneNumber.matches("^\\d{10}$")) {
            throw new InvalidUserDataException("Invalid phone number format. Must be 10 digits");
        }
    }
}
