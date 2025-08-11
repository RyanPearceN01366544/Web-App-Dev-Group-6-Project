package com.Group6.WebAppDevGroupProject.Controllers;

import com.Group6.WebAppDevGroupProject.Models.User;
import com.Group6.WebAppDevGroupProject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController // Marks this class as a REST controller for handling HTTP requests
@RequestMapping("/users") // Base URL path for all endpoints in this controller
@CrossOrigin // Allows requests from other origins (useful for frontend integration)
public class UserController {

    @Autowired
    private UserService userService; // Injects the UserService to interact with the DB

    /**
     * REGISTER NEW USER
     * POST /users/register
     * Accepts JSON body containing user details and saves it to the database
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Validation: username cannot be blank
        if (user.getUsername() == null || user.getUsername().trim().isEmpty())
            return ResponseEntity.badRequest().body("Username cannot be blank");

        // Validation: email cannot be blank
        if (user.getEmail() == null || user.getEmail().trim().isEmpty())
            return ResponseEntity.badRequest().body("Email cannot be blank");

        // Validation: password cannot be blank
        if (user.getPassword() == null || user.getPassword().trim().isEmpty())
            return ResponseEntity.badRequest().body("Password cannot be blank");

        // If role is not set, default to "customer"
        if (user.getRole() == null || user.getRole().trim().isEmpty())
            user.setRole("customer");

        // Check if email already exists
        if (userService.findByEmail(user.getEmail()).isPresent())
            return ResponseEntity.badRequest().body("Email already registered");

        // Check if username already exists
        if (userService.findByUsername(user.getUsername()).isPresent())
            return ResponseEntity.badRequest().body("Username already taken");

        // TODO: Hash password with BCrypt for security before saving
        User saved = userService.save(user);

        // Return a "201 Created" response with the new user's URI and data
        return ResponseEntity.created(URI.create("/users/" + saved.getUser_id())).body(saved);
    }

    /**
     * LOGIN USER
     * POST /users/login
     * Accepts JSON with username/email and password, returns role if valid
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginData) {
        // Ensure credentials are provided
        String id = loginData.getUsername(); // "username" field can be username OR email
        if (id == null || loginData.getPassword() == null)
            return ResponseEntity.badRequest().body("Missing credentials");

        // Determine whether to search by email or username
        var userOpt = id.contains("@")
                ? userService.findByEmail(id)
                : userService.findByUsername(id);

        // If user not found, return Unauthorized
        if (userOpt.isEmpty()) return ResponseEntity.status(401).body("Invalid credentials");

        var user = userOpt.get();

        // TODO: Compare hashed password with BCrypt instead of plain text
        if (!user.getPassword().equals(loginData.getPassword()))
            return ResponseEntity.status(401).body("Invalid credentials");

        // Return success message with user role
        return ResponseEntity.ok("Login successful as " + user.getRole());
    }

    /**
     * GET ALL USERS
     * GET /users
     * Returns a list of all registered users
     */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    /**
     * GET USER BY ID
     * GET /users/{id}
     * Returns the user details for the given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        return userService.findById(id)
                .map(ResponseEntity::ok) // If found, return user data
                .orElse(ResponseEntity.notFound().build()); // Else return 404
    }
}
