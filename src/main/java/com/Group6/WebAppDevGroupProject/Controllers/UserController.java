package com.Group6.WebAppDevGroupProject.Controllers;

import com.Group6.WebAppDevGroupProject.Models.User;
import com.Group6.WebAppDevGroupProject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.net.URI;
import java.util.List;

@Controller // Marks this class as a controller for handling web requests and returning views
@RequestMapping("/users") // Base URL path for all endpoints in this controller
@CrossOrigin // Allows requests from other origins (useful for frontend integration)
public class UserController {
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }
    // Default route redirects to login page
    @GetMapping("/")
    public String defaultToLogin() {
        return "login";
    }
    


    @Autowired
    private UserService userService; // Injects the UserService to interact with the DB

    /**
     * REGISTER NEW USER
     * POST /users/register
     * Accepts JSON body containing user details and saves it to the database
     */
    @PostMapping("/register")
    public String register(@ModelAttribute User user, org.springframework.ui.Model model) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            model.addAttribute("error", "Username cannot be blank");
            return "register";
        }
        // If role is not set, default to "student"
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("student");
        }
        // Check if username already exists
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            model.addAttribute("error", "Username already taken");
            return "register";
        }
        // TODO: Hash password with BCrypt for security before saving
        userService.save(user);
        model.addAttribute("message", "Registration successful! Please log in.");
        return "login";
    }

    /**
     * LOGIN USER
     * POST /users/login
     * Accepts form data with username/email and password
     */
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, org.springframework.ui.Model model, HttpSession session) {
        try {
            if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
                model.addAttribute("error", "Missing credentials");
                return "login";
            }
            
            var userOpt = username.contains("@")
                ? userService.findByEmail(username)
                : userService.findByUsername(username);
                
            if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(password)) {
                model.addAttribute("error", "Invalid credentials");
                return "login";
            }
            
            // Store user in session
            session.setAttribute("user", userOpt.get());
            return "redirect:/menu";
        } catch (Exception e) {
            model.addAttribute("error", "Login failed: " + e.getMessage());
            return "login";
        }
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

    /**
     * LOGOUT USER
     * GET /logout
     * Invalidates the session and redirects to login
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
