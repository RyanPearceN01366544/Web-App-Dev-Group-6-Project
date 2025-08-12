package com.Group6.WebAppDevGroupProject.Controllers;

import com.Group6.WebAppDevGroupProject.Models.User;
import com.Group6.WebAppDevGroupProject.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/users") // Base URL path for all endpoints in this controller
@CrossOrigin // Allows requests from other origins (useful for frontend integration)
public class UserController {
    @Autowired
    private UserService userService; // Injects the UserService to interact with the DB

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping({"/register", "/Register"})
    public String register(Model model_)
    {
        model_.addAttribute("user", new User());
        return "user-register";
    }
    @PostMapping({"/register", "/Register"})
    public String registering(Model model_, @ModelAttribute User user) {
        // Validation: username cannot be blank
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Error: Your username cannot be blank!");
        }

        // Validation: email cannot be blank
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()){
            throw new RuntimeException("Error: Your email cannot be blank!");
        }

        // Validation: password cannot be blank
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Error: Your password can't be blank!");
        }

        // If role is not set, default to "customer"
        if (user.getRole() == null || user.getRole().trim().isEmpty())
            user.setRole("customer");

        // Check if email already exists
        if (userService.findByEmail(user.getEmail()).isPresent()){
            throw new RuntimeException("Error: Your email is already in use!");
        }

        // Check if username already exists
        if (userService.findByUsername(user.getUsername()).isPresent()){
            throw new RuntimeException("Error: Your username is already in use!");
        }

        // TODO: Hash password with BCrypt for security before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User saved = userService.save(user);
        return "redirect:/orders/new";
    }

    /**
     * LOGIN USER
     * POST /users/login
     * Accepts JSON with username/email and password, returns role if valid
     */
    @GetMapping({"/login", "/Login"})
    public String login(Model model_) {
        model_.addAttribute("user", new User());
        return "user-login";
    }
    @PostMapping({"/login", "/Login"})
    public String login(Model model_, @ModelAttribute User loginData) {
        // Ensure credentials are provided
        String id = loginData.getUsername(); // "username" field can be username OR email
        if (id == null || loginData.getPassword() == null){
            throw new RuntimeException("Error: Your password is incorrect!");
        }

        // Determine whether to search by email or username
        var userOpt = id.contains("@")
                ? userService.findByEmail(id)
                : userService.findByUsername(id);

        // If user not found, return Unauthorized
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Error: Invalid Credentials!");
        }

        var user = userOpt.get();

        // TODO: Compare hashed password with BCrypt instead of plain text
        if (!passwordEncoder.matches(loginData.getPassword(), user.getPassword())) {
            throw new RuntimeException("Error: Invalid Credentials!");
        }
        return "redirect:/orders/active";
    }

    /**
     * GET ALL USERS
     * GET /users
     * Returns a list of all registered users
     */
    @GetMapping({"/list", "/List"})
    public String getAllUsers(Principal auth_, Model modeL_) {
        modeL_.addAttribute("users", userService.findAll());
        return "users-list";
    }

    /**
     * GET USER BY ID
     * GET /users/{id}
     * Returns the user details for the given ID
     */
    @GetMapping({"/list/{id}", "/List/{id}"})
    public String getUserById(Model model_, @PathVariable("id") int id) {
        model_.addAttribute("user", userService.findById(id));
        return "users-view";
    }

    @PostMapping({"/logout", "/Logout"})
    public String logout(){

        return "users-logout";
    }
}
