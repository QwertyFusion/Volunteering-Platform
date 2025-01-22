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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.crypto.password.PasswordEncoder;

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
	@Transactional
	public ResponseEntity<Organization> registerOrganization(@RequestBody @Valid OrganizationDto orgDTO) {
		try {
			Organization org = new Organization();
			org.setName(orgDTO.getName());
			org.setEmail(orgDTO.getEmail());
			org.setPassword(orgDTO.getPassword());
			org.setPhoneNumber(orgDTO.getPhoneNumber());
			org.setAddress(orgDTO.getAddress());
			org.setWebsite(orgDTO.getWebsite());
			userService.saveUser(org);
			return new ResponseEntity<>(org, HttpStatus.CREATED);
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

	@PutMapping("/organizations/{organizationId}")
    @Transactional
    public ResponseEntity<Organization> updateOrganizationById(
            @PathVariable Long organizationId, 
            @RequestBody @Valid OrganizationPartialDto updatedOrg) {
        
        Organization existingOrg = userService.findOrganizationById(organizationId).orElse(null);
        if (existingOrg == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Set updated fields, or retain existing ones if not updated
        existingOrg.setName(updatedOrg.getName() != null ? updatedOrg.getName() : existingOrg.getName());
        existingOrg.setEmail(updatedOrg.getEmail() != null ? updatedOrg.getEmail() : existingOrg.getEmail());
        existingOrg.setPassword(updatedOrg.getPassword() != null ? passwordEncoder.encode(updatedOrg.getPassword()) : existingOrg.getPassword());
        existingOrg.setPhoneNumber(updatedOrg.getPhoneNumber() != null ? updatedOrg.getPhoneNumber() : existingOrg.getPhoneNumber());
        existingOrg.setAddress(updatedOrg.getAddress() != null ? updatedOrg.getAddress() : existingOrg.getAddress());
        existingOrg.setWebsite(updatedOrg.getWebsite() != null ? updatedOrg.getWebsite() : existingOrg.getWebsite());

        userService.saveOrganization(existingOrg);  // Save the updated organization
        return new ResponseEntity<>(existingOrg, HttpStatus.OK);
    }

	/**
	 * Delete an organization by its ID.
	 *
	 * @param organizationId Organization ID.
	 * @return HTTP 204 if deleted, HTTP 404 if not found.
	 */
	@DeleteMapping("/organizations/{organizationId}")
	@Transactional
	public ResponseEntity<Void> deleteOrganizationById(@PathVariable Long organizationId) {
		Optional<Organization> organizationOpt = userService.findOrganizationById(organizationId);
		if (organizationOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Organization organization = organizationOpt.get();
		List<Task> tasks = organization.getTasks();

		for (Task task : tasks) {
			List<TaskSignup> signups = taskSignupService.getTaskSignups(task.getId());

			for (TaskSignup signup : signups) {
				taskSignupService.deleteById(signup.getSignupId());
			}

			taskService.deleteByTaskId(task.getId());
		}

		userService.deleteUserById(organizationId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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

	@PutMapping("/volunteers/{volunteerId}")
    @Transactional
    public ResponseEntity<Volunteer> updateVolunteerById(
            @PathVariable Long volunteerId, 
            @RequestBody @Valid VolunteerPartialDto updatedVol) {

        Volunteer existingVol = userService.findVolunteerById(volunteerId).orElse(null);
        if (existingVol == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Set updated fields, or retain existing ones if not updated
        existingVol.setName(updatedVol.getName() != null ? updatedVol.getName() : existingVol.getName());
        existingVol.setEmail(updatedVol.getEmail() != null ? updatedVol.getEmail() : existingVol.getEmail());
        existingVol.setPassword(updatedVol.getPassword() != null ? passwordEncoder.encode(updatedVol.getPassword()) : existingVol.getPassword());
        existingVol.setPhoneNumber(updatedVol.getPhoneNumber() != null ? updatedVol.getPhoneNumber() : existingVol.getPhoneNumber());

        userService.saveVolunteer(existingVol);  // Save the updated volunteer
        return new ResponseEntity<>(existingVol, HttpStatus.OK);
    }

	/**
	 * Delete a volunteer by their ID.
	 *
	 * @param volunteerId Volunteer ID.
	 * @return HTTP 204 if deleted, HTTP 404 if not found.
	 */
	@DeleteMapping("/volunteers/{volunteerId}")
	@Transactional
	public ResponseEntity<Void> deleteVolunteerById(@PathVariable Long volunteerId) {
		Optional<Volunteer> volunteerOpt = userService.findVolunteerById(volunteerId);
		if (volunteerOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		List<TaskSignup> signups = taskSignupService.getUserSignups(volunteerId);

		for (TaskSignup signup : signups) {
			taskSignupService.deleteById(signup.getSignupId());
		}

		userService.deleteUserById(volunteerId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	// Authentication for login (existing methods remain unchanged)

    @PostMapping("/login/volunteers")
    public ResponseEntity<Object> loginVolunteer(@RequestBody @Valid LoginDto loginDto) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok().body("Logged in as " + userDetails.getUsername());
    }

    @PostMapping("/login/organizations")
    public ResponseEntity<Object> loginOrganization(@RequestBody @Valid LoginDto loginDto) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Invalid email or password", HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return ResponseEntity.ok().body("Logged in as " + userDetails.getUsername());
    }
}
