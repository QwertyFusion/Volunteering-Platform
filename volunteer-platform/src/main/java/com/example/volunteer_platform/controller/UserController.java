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
@RequestMapping
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/users")
	public ResponseEntity<?> getAllUsers() {
		List<User> allUsers = userService.getAllUsers();
		if (allUsers.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}

	@GetMapping("/organizations ")
	public ResponseEntity<List<Organization>> getAllOrganizations() {
		List<Organization> organizations = userService.getAllOrganizations();
		if (organizations.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(organizations, HttpStatus.OK);
	}

	// View all Volunteers
	@GetMapping("/volunteers")
	public ResponseEntity<List<Volunteer>> getAllVolunteers() {
		List<Volunteer> volunteers = userService.getAllVolunteers();
		if (volunteers.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(volunteers, HttpStatus.OK);
	}

	@PostMapping("/organizations")
	public ResponseEntity<Organization> registerOrganization(@RequestBody Organization org) {
		try {
			userService.saveUser(org);
			return new ResponseEntity<>(org, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/volunteers")
	public ResponseEntity<Volunteer> registerVolunteer(@RequestBody Volunteer volunteer) {
		try {
			userService.saveUser (volunteer);
			return new ResponseEntity<>(volunteer, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/users/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Long userId) {
		Optional<User> user = userService.findUserById(userId);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

	@DeleteMapping("/users/{userId}")
	public ResponseEntity<Void> deleteUserById(@PathVariable Long userId) {
		Optional<User> user = userService.findUserById(userId);
		if (user.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		userService.deleteUserById(userId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	//	WE CANNOT EDIT USER - We can edit or update organization or volunteer

	@PutMapping("/organizations/{organizationId}")
	public ResponseEntity<Organization> updateOrganizationById(@PathVariable Long organizationId, @RequestBody Organization updatedOrg) {
		Organization existingOrg = userService.findOrganizationById(organizationId).orElse(null);
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
	@PutMapping("/volunteers/{volunteerId}")
	public ResponseEntity<Volunteer> updateVolunteerById(@PathVariable Long volunteerId, @RequestBody Volunteer updatedVol) {
		Volunteer existingVol = userService.findVolunteerById(volunteerId).orElse(null);
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
