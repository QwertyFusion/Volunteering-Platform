package com.example.volunteer_platform.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.volunteer_platform.model.User;
import com.example.volunteer_platform.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/all")
	public ResponseEntity<?> getAllUsers() {
		List<User> allUsers = userService.getAllUsers();
		if (allUsers.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}

	@PostMapping("/register")
	public ResponseEntity<User> registerUser(@RequestBody User user) {
		try {
			userService.saveUser(user);
			return new ResponseEntity<>(user, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/id/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		Optional<User> user = userService.findUserById(id);
		if (user.isPresent()) {
			return new ResponseEntity<>(user.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/delete/id/{id}")
	public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
		Optional<User> user = userService.findUserById(id);
		if (user.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		userService.deleteUserById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping("/edit/id/{id}")
	public ResponseEntity<User> updateUserById(@PathVariable Long id, @RequestBody User updatedUser) {
	    // Fetch the existing user
	    User existingUser = userService.findUserById(id).orElse(null);
	    if (existingUser == null) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }

	    // Update the fields (you can add specific fields for updating here)
	    existingUser.setName(updatedUser.getName()!=null && !updatedUser.getName().isEmpty()? updatedUser.getName() : existingUser.getName());
	    existingUser.setEmail(updatedUser.getEmail()!=null && !updatedUser.getEmail().isEmpty()? updatedUser.getEmail() : existingUser.getEmail());
	    // We cannot update roles of existing user
	    existingUser.setPassword(updatedUser.getPassword()!=null && !updatedUser.getPassword().isEmpty()? updatedUser.getPassword() : existingUser.getPassword());  // Update the password if needed

	    // Save the updated user to the database
	    userService.saveUser(existingUser);

		return new ResponseEntity<>(existingUser, HttpStatus.OK);  // Return the updated user
	}

}
