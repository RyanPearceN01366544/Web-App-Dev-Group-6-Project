package com.Group6.WebAppDevGroupProject.Service;

import com.Group6.WebAppDevGroupProject.Models.User;
import com.Group6.WebAppDevGroupProject.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * UserService class
 * Acts as the business logic layer between the UserController and UserRepository.
 * Handles saving, retrieving, and validating user data.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository; // Injects UserRepository for DB access

    /**
     * Save a user to the database.
     * @param u User object to save.
     * @return The saved user object.
     */
    public User save(User u) {
        return userRepository.save(u);
    }

    /**
     * Find a user by email.
     * @param email The user's email.
     * @return Optional containing the user if found.
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Find a user by username.
     * @param username The user's username.
     * @return Optional containing the user if found.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Get a list of all users.
     * @return List of users.
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Find a user by their ID.
     * @param id The user's ID.
     * @return Optional containing the user if found.
     */
    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }
}
