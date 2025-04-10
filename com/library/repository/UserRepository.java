package com.library.repository;

import com.library.model.User;
import com.library.model.UserType;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for User entities.
 */
public interface UserRepository {
    /**
     * Saves a user to the repository.
     * 
     * @param user The user to save
     * @return The saved user
     */
    User save(User user);
    
    /**
     * Finds a user by their ID.
     * 
     * @param id The ID of the user to find
     * @return An Optional containing the user if found, or empty if not found
     */
    Optional<User> findById(String id);
    
    /**
     * Checks if a user with the given ID exists.
     * 
     * @param id The ID to check
     * @return true if a user with the ID exists, false otherwise
     */
    boolean existsById(String id);
    
    /**
     * Deletes a user by their ID.
     * 
     * @param id The ID of the user to delete
     */
    void deleteById(String id);
    
    /**
     * Finds all users in the repository.
     * 
     * @return A list of all users
     */
    List<User> findAll();
    
    /**
     * Finds a user by their email.
     * 
     * @param email The email of the user to find
     * @return An Optional containing the user if found, or empty if not found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Finds users by their type.
     * 
     * @param userType The type to filter by
     * @return A list of users of the specified type
     */
    List<User> findByUserType(UserType userType);
    
    /**
     * Finds users with first names or last names containing the given keyword (case-insensitive).
     * 
     * @param firstNameKeyword The keyword to search for in first names
     * @param lastNameKeyword The keyword to search for in last names
     * @return A list of users with names containing the keyword
     */
    List<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstNameKeyword, String lastNameKeyword);
    
    /**
     * Finds users with locked accounts.
     * 
     * @param locked The locked status to filter by
     * @return A list of users with the specified locked status
     */
    List<User> findByAccountLocked(boolean locked);
    
    /**
     * Finds users with fine amounts greater than the specified amount.
     * 
     * @param amount The amount to filter by
     * @return A list of users with fine amounts greater than the specified amount
     */
    List<User> findByFineAmountGreaterThan(double amount);
}
