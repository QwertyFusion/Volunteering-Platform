package com.example.volunteer_platform.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.volunteer_platform.model.User;
import com.example.volunteer_platform.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User savedUser = userService.registerUser(user);  // Register the user with encoded password
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);  // 201 Created for successful registration
    }

    // Get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Return 404 if not found
    }

    // Delete a user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();  // Return 204 No Content after successful deletion
    }

    // Update user details
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        // Fetch the existing user
        User existingUser = userService.getUserById(id);  
        if (existingUser == null) {
            return ResponseEntity.notFound().build();  // If the user is not found, return 404
        }

        // Update the fields (you can add specific fields for updating here)
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setRole(updatedUser.getRole());

        // Update the password (if it's provided and needs to be changed)
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(updatedUser.getPassword());  // You can add encoding here
        }

        // Save the updated user to the database
        User savedUser = userService.saveUser(existingUser);

        return ResponseEntity.ok(savedUser);  // Return the updated user with 200 OK
    }
}
