package com.example.volunteer_platform.controller;

import com.example.volunteer_platform.model.Organization;
import com.example.volunteer_platform.model.Volunteer;
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

	@GetMapping("/all/organizations")
	public ResponseEntity<List<Organization>> getAllOrganizations() {
		List<Organization> organizations = userService.getAllOrganizations();
		if (organizations.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(organizations, HttpStatus.OK);
	}

	// View all Volunteers
	@GetMapping("/all/volunteers")
	public ResponseEntity<List<Volunteer>> getAllVolunteers() {
		List<Volunteer> volunteers = userService.getAllVolunteers();
		if (volunteers.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(volunteers, HttpStatus.OK);
	}

//	WE DO NOT NEED A USER - We need them to be either organization or volunteer
//	@PostMapping("/register")
//	public ResponseEntity<User> registerUser(@RequestBody User user) {
//		try {
//			userService.saveUser(user);
//			return new ResponseEntity<>(user, HttpStatus.CREATED);
//		} catch (Exception e) {
//			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//		}
//	}

	@PostMapping("/register/organization")
	public ResponseEntity<Organization> registerOrganization(@RequestBody Organization org) {
		try {
			userService.saveUser(org);
			return new ResponseEntity<>(org, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/register/volunteer")
	public ResponseEntity<Volunteer> registerVolunteer(@RequestBody Volunteer volunteer) {
		try {
			userService.saveUser (volunteer);
			return new ResponseEntity<>(volunteer, HttpStatus.CREATED);
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

//	WE CANNOT EDIT USER - We can edit or update organization or volunteer
//	@PutMapping("/edit/id/{id}")
//	public ResponseEntity<User> updateUserById(@PathVariable Long id, @RequestBody User updatedUser) {
//	    // Fetch the existing user
//	    User existingUser = userService.findUserById(id).orElse(null);
//	    if (existingUser == null) {
//	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//	    }
//
//	    // Update the fields (you can add specific fields for updating here)
//	    existingUser.setName(updatedUser.getName()!=null && !updatedUser.getName().isEmpty()? updatedUser.getName() : existingUser.getName());
//	    existingUser.setEmail(updatedUser.getEmail()!=null && !updatedUser.getEmail().isEmpty()? updatedUser.getEmail() : existingUser.getEmail());
//	    // We cannot update roles of existing user
//	    existingUser.setPassword(updatedUser.getPassword()!=null && !updatedUser.getPassword().isEmpty()? updatedUser.getPassword() : existingUser.getPassword());  // Update the password if needed
//
//	    // Save the updated user to the database
//	    userService.saveUser(existingUser);
//
//		return new ResponseEntity<>(existingUser, HttpStatus.OK);  // Return the updated user
//	}

	@PutMapping("/edit/organization/id/{id}")
	public ResponseEntity<Organization> updateOrganizationById(@PathVariable Long id, @RequestBody Organization updatedOrg) {
		Organization existingOrg = userService.findOrganizationById(id).orElse(null);
		if (existingOrg == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		existingOrg.setName(updatedOrg.getName() != null ? updatedOrg.getName() : existingOrg.getName());
		existingOrg.setEmail(updatedOrg.getEmail() != null ? updatedOrg.getEmail() : existingOrg.getEmail());
		existingOrg.setPassword(updatedOrg.getPassword() != null ? updatedOrg.getPassword() : existingOrg.getPassword());
		existingOrg.setPhoneNumber(updatedOrg.getPhoneNumber() != null ? updatedOrg.getPhoneNumber() : existingOrg.getPhoneNumber());
		existingOrg.setAddress(updatedOrg.getAddress() != null ? updatedOrg.getAddress() : existingOrg.getAddress());
		existingOrg.setWebsite(updatedOrg.getWebsite() != null ? updatedOrg.getWebsite() : existingOrg.getWebsite());

		userService.saveUser (existingOrg);
		return new ResponseEntity<>(existingOrg, HttpStatus.OK);
	}

	// Edit Volunteer
	@PutMapping("/edit/volunteer/id/{id}")
	public ResponseEntity<Volunteer> updateVolunteerById(@PathVariable Long id, @RequestBody Volunteer updatedVol) {
		Volunteer existingVol = userService.findVolunteerById(id).orElse(null);
		if (existingVol == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		existingVol.setName(updatedVol.getName() != null ? updatedVol.getName() : existingVol.getName());
		existingVol.setEmail(updatedVol.getEmail() != null ? updatedVol.getEmail() : existingVol.getEmail());
		existingVol.setPassword(updatedVol.getPassword() != null ? updatedVol.getPassword() : existingVol.getPassword());
		existingVol.setPhoneNumber(updatedVol.getPhoneNumber() != null ? updatedVol.getPhoneNumber() : existingVol.getPhoneNumber());

		userService.saveUser (existingVol);
		return new ResponseEntity<>(existingVol, HttpStatus.OK);
	}
}
