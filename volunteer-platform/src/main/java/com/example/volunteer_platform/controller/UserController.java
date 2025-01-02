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

	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody User user) {
		 User savedUser= userService.registerUser(user);
		return new ResponseEntity<>(savedUser,HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(@PathVariable Long id) {
		Optional<User> user = userService.findById(id);
		if (user.isPresent()) {
			return new ResponseEntity<>(user.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
	    // Fetch the existing user
	    User existingUser = userService.getUserById(id);  
	    if (existingUser == null) {
	        return ResponseEntity.notFound().build();  // If the user is not found
	    }

	    // Update the fields (you can add specific fields for updating here)
	    existingUser.setName(updatedUser.getName());
	    existingUser.setEmail(updatedUser.getEmail());
	    existingUser.setRole(updatedUser.getRole());
	    existingUser.setPassword(updatedUser.getPassword());  // Update the password if needed


	    // Save the updated user to the database
	    User savedUser = userService.saveUser(existingUser);

	    return ResponseEntity.ok(savedUser);  // Return the updated user
	}

}
