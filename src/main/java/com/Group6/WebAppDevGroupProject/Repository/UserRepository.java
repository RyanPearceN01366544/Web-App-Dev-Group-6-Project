package com.Group6.WebAppDevGroupProject.Repository;

import com.Group6.WebAppDevGroupProject.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * UserRepository interface
 * Extends JpaRepository to provide CRUD operations for the User entity.
 * The primary key type for User is Integer (user_id).
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their email address.
     * @param email The user's email.
     * @return Optional containing the user if found, otherwise empty.
     */
    @Query(value = "SELECT * FROM user WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);

    /**
     * Find a user by their username.
     * @param username The user's username.
     * @return Optional containing the user if found, otherwise empty.
     */
    @Query(value = "SELECT * FROM user WHERE username = :username", nativeQuery = true)
    Optional<User> findByUsername(@Param("username") String username);
}
