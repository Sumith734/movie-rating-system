package com.review.movie.controller;

import com.review.movie.entity.User;
import com.review.movie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User user) {
        try {
            // Check if username already exists
            if (userService.existsByUsername(user.getUsername())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Username already exists");
                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
            }

            // Check if email already exists
            if (userService.existsByEmail(user.getEmail())) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Email already exists");
                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
            }

            User savedUser = userService.register(user);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("user", savedUser);
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Registration failed");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Login user (simple version without JWT)
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, String> loginRequest) {
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            Optional<User> userOpt = userService.findByUsername(username);
            
            if (userOpt.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "User not found");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }

            User user = userOpt.get();
            
            // Simple password verification (in production, use proper password hashing)
            if (!user.getPassword().equals(password)) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid credentials");
                return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login successful");
            response.put("user", user);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Login failed");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get user by username
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        try {
            Optional<User> userOpt = userService.findByUsername(username);
            
            if (userOpt.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            return new ResponseEntity<>(userOpt.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Check if username exists
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@PathVariable String username) {
        try {
            boolean exists = userService.existsByUsername(username);
            Map<String, Boolean> response = new HashMap<>();
            response.put("exists", exists);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Check if email exists
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Map<String, Boolean>> checkEmail(@PathVariable String email) {
        try {
            boolean exists = userService.existsByEmail(email);
            Map<String, Boolean> response = new HashMap<>();
            response.put("exists", exists);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get user profile (returns user without password for security)
    @GetMapping("/profile/{username}")
    public ResponseEntity<Map<String, Object>> getUserProfile(@PathVariable String username) {
        try {
            Optional<User> userOpt = userService.findByUsername(username);
            
            if (userOpt.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            
            User user = userOpt.get();
            Map<String, Object> profile = new HashMap<>();
            profile.put("id", user.getId());
            profile.put("username", user.getUsername());
            profile.put("email", user.getEmail());
            profile.put("role", user.getRole());
            profile.put("createdAt", user.getCreatedAt());
            profile.put("totalRatings", user.getRatings().size());
            
            return new ResponseEntity<>(profile, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
