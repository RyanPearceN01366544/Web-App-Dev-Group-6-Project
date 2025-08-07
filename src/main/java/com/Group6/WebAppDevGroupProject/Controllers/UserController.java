package com.Group6.WebAppDevGroupProject.Controllers;

import com.Group6.WebAppDevGroupProject.Models.User;
import com.Group6.WebAppDevGroupProject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    // Register new user
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Username cannot be blank");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email cannot be blank");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Password cannot be blank");
        }
        // Optional: check if username or email already exists (not shown here)
        //User savedUser = userService.save(user); // --> Temporary
        //return ResponseEntity.ok(savedUser); // --> Temporary
        return null; // Temporary
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginData) {
        //User user = userService.findByUsername(loginData.getUsername());
        User user = new User(); // Temporary
        if (user != null && user.getPassword().equals(loginData.getPassword())) {
            return ResponseEntity.ok("Login successful as " + user.getRole());
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        //return userService.findAll();
        return null; // Temporary
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        //return userService.findById(id)
        //        .map(ResponseEntity::ok)
        //        .orElse(ResponseEntity.notFound().build());
        return null; // Temporary
    }
}
