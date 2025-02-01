package com.example.volunteer_platform.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.volunteer_platform.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.volunteer_platform.dto.*;
import com.example.volunteer_platform.model.*;
import com.example.volunteer_platform.service.TaskService;
import com.example.volunteer_platform.service.TaskSignupService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * UserController handles API endpoints for managing users, organizations, and volunteers
 */
@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService userService;
	
	

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskSignupService taskSignupService;

	// User APIs (Both organizations and volunteers)
	/**
	 * Get all users in the system.
	 *
	 * @return List of users or HTTP 204 if no users exist.
	 */
	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
		List<User> allUsers = userService.getAllUsers();
		if (allUsers.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}

	/**
	 * Get a user by their ID.
	 *
	 * @param userId User ID.
	 * @return User details or HTTP 404 if not found.
	 */
	@GetMapping("/users/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Long userId) {
		Optional<User> user = userService.findUserById(userId);
		return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	// Organization APIs
	/**
	 * Get all organizations.
	 *
	 * @return List of organizations or HTTP 204 if none exist.
	 */
	@GetMapping("/organizations")
	public ResponseEntity<List<Organization>> getAllOrganizations() {
		List<Organization> organizations = userService.getAllOrganizations();
		if (organizations.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(organizations, HttpStatus.OK);
	}

	/**
	 * Register a new organization.
	 *
	 * @param orgDTO Organization details
	 * @return Registered organization or HTTP 400 for invalid input.
	 */
	@PostMapping("/organizations")
	public ResponseEntity<OrganizationDto> registerOrganization(@RequestBody @Valid OrganizationDto orgDTO) {
		try {
			userService.saveOrganization(orgDTO);
			return new ResponseEntity<>(orgDTO, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Get an organization by its ID.
	 *
	 * @param orgId Organization ID.
	 * @return Organization details or HTTP 404 if not found.
	 */
	@GetMapping("/organizations/{orgId}")
	public ResponseEntity<Organization> getOrganizationById(@PathVariable Long orgId) {
		Optional<Organization> org = userService.findOrganizationById(orgId);
		return org.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * Update an organization's details.
	 *
	 * @param organizationId Organization ID.
	 * @param updatedOrg Updated organization details.
	 * @return Updated organization or HTTP 404 if not found.
	 */
	@PutMapping("/organizations/{organizationId}")
	public ResponseEntity<Organization> updateOrganizationById(@PathVariable Long organizationId, @RequestBody @Valid OrganizationPartialDto updatedOrg) {
		try {
			Optional<Organization> updatedOrganization = userService.updateOrganization(organizationId, updatedOrg);
			return updatedOrganization.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
					.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Delete an organization by its ID.
	 *
	 * @param organizationId Organization ID.
	 * @return HTTP 204 if deleted, HTTP 404 if not found.
	 */
	@DeleteMapping("/organizations/{organizationId}")
	public ResponseEntity<Void> deleteOrganizationById(@PathVariable Long organizationId) {
		try {
			if (userService.deleteOrganizationById(organizationId)) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Volunteer APIs
	/**
	 * Get all volunteers.
	 *
	 * @return List of volunteers or HTTP 204 if none exist.
	 */
	@GetMapping("/volunteers")
	public ResponseEntity<List<Volunteer>> getAllVolunteers() {
		List<Volunteer> volunteers = userService.getAllVolunteers();
		if (volunteers.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(volunteers, HttpStatus.OK);
	}

	/**
	 * Register a new volunteer.
	 *
	 * @param volunteerDTO Volunteer details.
	 * @return Registered volunteer or HTTP 400 for invalid input.
	 */
	@PostMapping("/volunteers")
	public ResponseEntity<VolunteerDto> registerVolunteer(@RequestBody @Valid VolunteerDto volunteerDTO) {
		try {
			userService.saveVolunteer(volunteerDTO);
			return new ResponseEntity<>(volunteerDTO, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Get a volunteer by their ID.
	 *
	 * @param volunteerId Volunteer ID.
	 * @return Volunteer details or HTTP 404 if not found.
	 */
	@GetMapping("/volunteers/{volunteerId}")
	public ResponseEntity<Volunteer> getVolunteerById(@PathVariable Long volunteerId) {
		Optional<Volunteer> volunteer = userService.findVolunteerById(volunteerId);
		return volunteer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
				.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}


	
	/**
	 * Update a volunteer's details.
	 *
	 * @param volunteerId Volunteer ID.
	 * @param updatedVol Updated volunteer details.
	 * @return Updated volunteer or HTTP 404 if not found.
	 */
	@PutMapping("/volunteers/{volunteerId}")
	public ResponseEntity<Volunteer> updateVolunteerById(@PathVariable Long volunteerId, @RequestBody @Valid VolunteerPartialDto updatedVol) {
		try {
			Optional<Volunteer> updatedVolunteer = userService.updateVolunteer(volunteerId, updatedVol);
			return updatedVolunteer.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
					.orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Delete a volunteer by their ID.
	 *
	 * @param volunteerId Volunteer ID.
	 * @return HTTP 204 if deleted, HTTP 404 if not found.
	 */
	@DeleteMapping("/volunteers/{volunteerId}")
	public ResponseEntity<Void> deleteVolunteerById(@PathVariable Long volunteerId) {
		try {
			if (userService.deleteVolunteerById(volunteerId)) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (RuntimeException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	
	@GetMapping("/volunteers/email/{email}")
	public ResponseEntity<Volunteer> findVolunteerByEmailOptional(@PathVariable String email) {
	    Optional<Volunteer> volunteerOpt = userService.findVolunteerByEmailOptional(email);
	    
	    return volunteerOpt.map(ResponseEntity::ok) // If volunteer is found, return 200 OK
	                      .orElseGet(() -> ResponseEntity.notFound().build()); // If not found, return 404
	}

}
