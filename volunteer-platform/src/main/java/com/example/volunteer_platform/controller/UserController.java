package com.example.volunteer_platform.controller;

import com.example.volunteer_platform.dto.*;
import com.example.volunteer_platform.model.*;
import com.example.volunteer_platform.service.SkillService;
import com.example.volunteer_platform.service.TaskService;
import com.example.volunteer_platform.service.TaskSignupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.volunteer_platform.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private SkillService skillService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskSignupService taskSignupService;

	@GetMapping("/users")
	public ResponseEntity<?> getAllUsers() {
		List<User> allUsers = userService.getAllUsers();
		if (allUsers.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}

	@GetMapping("/organizations")
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

	// Example Request Body for registerOrganization
	//	{
	//	    "name": "Buy me coffee",
	//	    "email": "info@buymecoffee.com",
	//	    "password": "Coffee1234",
	//	    "phoneNumber": "+91 xxxxxxxxxx",
	//	    "address": "Example Road, Kolkata",
	//	    "website": "www.example.com"
	//	}
	@PostMapping("/organizations")
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
			userService.saveUser(org);
			return new ResponseEntity<>(org, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// Example Request Body for registerVolunteer
	//	{
	//		"name": "Full name here",
	//			"email": "email here",
	//			"password": "password here",
	//			"phoneNumber": "+91 xxxxxxxxxx",
	//			"gender": "MALE or FEMALE or OTHER"
	//	}
	@PostMapping("/volunteers")
	public ResponseEntity<Volunteer> registerVolunteer(@RequestBody VolunteerDto volunteerDTO) {
		try {
			Volunteer volunteer = new Volunteer();
			volunteer.setName(volunteerDTO.getName());
			volunteer.setEmail(volunteerDTO.getEmail());
			volunteer.setPassword(volunteerDTO.getPassword());
			volunteer.setPhoneNumber(volunteerDTO.getPhoneNumber());
			volunteer.setGender(volunteerDTO.getGender());
			userService.saveUser(volunteer);
			return new ResponseEntity<>(volunteer, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// Universal
	@GetMapping("/users/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Long userId) {
		Optional<User> user = userService.findUserById(userId);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

	// Role based
	@GetMapping("/organizations/{orgId}")
	public ResponseEntity<Organization> getOrganizationById(@PathVariable Long orgId) {
		Optional<Organization> org = userService.findOrganizationById(orgId);
        return org.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@GetMapping("/volunteers/{volunteerId}")
	public ResponseEntity<Volunteer> getVolunteerById(@PathVariable Long volunteerId) {
		Optional<Volunteer> volunteer = userService.findVolunteerById(volunteerId);
        return volunteer.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("/volunteers/{volunteerId}")
	public ResponseEntity<Void> deleteVolunteerById(@PathVariable Long volunteerId) {
		Optional<Volunteer> volunteerOpt = userService.findVolunteerById(volunteerId);
		if (volunteerOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// Retrieve all task signups for the volunteer
		List<TaskSignup> signups = taskSignupService.getUserSignups(volunteerId);

		// Delete all signups associated with the volunteer
		for (TaskSignup signup : signups) {
			taskSignupService.deleteById(signup.getSignupId());
		}

		// Now delete the volunteer
		userService.deleteUserById(volunteerId);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/organizations/{organizationId}")
	public ResponseEntity<Void> deleteOrganizationById(@PathVariable Long organizationId) {
		Optional<Organization> organizationOpt = userService.findOrganizationById(organizationId);
		if (organizationOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Organization organization = organizationOpt.get();

		// Retrieve all tasks for the organization
		List<Task> tasks = organization.getTasks();

		// Delete all signups and tasks associated with the organization
		for (Task task : tasks) {
			// Retrieve all signups for the task
			List<TaskSignup> signups = taskSignupService.getTaskSignups(task.getId());

			// Delete all signups associated with the task
			for (TaskSignup signup : signups) {
				taskSignupService.deleteById(signup.getSignupId());
			}

			// Now delete the task
			taskService.deleteByTaskId(task.getId());
		}

		// Finally, delete the organization
		userService.deleteUserById(organizationId);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	//	WE CANNOT EDIT USER - We can edit or update organization or volunteer

	// Edit Organization
	@PutMapping("/organizations/{organizationId}")
	public ResponseEntity<Organization> updateOrganizationById(@PathVariable Long organizationId, @RequestBody OrganizationPartialDto updatedOrg) {
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
	public ResponseEntity<Volunteer> updateVolunteerById(@PathVariable Long volunteerId, @RequestBody VolunteerPartialDto updatedVol) {
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

	// Add Skill to Volunteer
	@PostMapping("/volunteers/{volunteerId}/skills")
	public ResponseEntity<Volunteer> addSkillToVolunteer(@PathVariable Long volunteerId, @RequestBody SkillDto skillDto) {
		Optional<Volunteer> volunteerOpt = userService.findVolunteerById(volunteerId);
		if (volunteerOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Volunteer volunteer = volunteerOpt.get();
		Skill skill = skillService.findByName(skillDto.getName().toLowerCase()).orElse(null);

		if (skill == null) {
			// Create new skill if it doesn't exist
			skill = new Skill();
			skill.setName(skillDto.getName().toLowerCase());
			skillService.saveSkill(skill);
		}

		// Add skill to volunteer
		volunteer.getSkills().add(skill);
		userService.saveUser(volunteer);
		return new ResponseEntity<>(volunteer, HttpStatus.OK);
	}

	// Remove Skill from Volunteer
	@DeleteMapping("/volunteers/{volunteerId}/skills/{skillId}")
	public ResponseEntity<Volunteer> removeSkillFromVolunteer(@PathVariable Long volunteerId, @PathVariable Long skillId) {
		Optional<Volunteer> volunteerOpt = userService.findVolunteerById(volunteerId);
		if (volunteerOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Volunteer volunteer = volunteerOpt.get();
		Skill skill = skillService.findById(skillId).orElse(null);

		if (skill == null || !volunteer.getSkills().contains(skill)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// Remove skill from volunteer
		volunteer.getSkills().remove(skill);
		userService.saveUser (volunteer);
		return new ResponseEntity<>(volunteer, HttpStatus.OK);
	}

	// Get Volunteer skills by volunteer id
	@GetMapping("/volunteers/{volunteerId}/skills")
	public ResponseEntity<List<Skill>> getVolunteerSkills(@PathVariable Long volunteerId) {
		Optional<Volunteer> volunteerOpt = userService.findVolunteerById(volunteerId);
		if (volunteerOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		Volunteer volunteer = volunteerOpt.get();
		List<Skill> skills = new ArrayList<>(volunteer.getSkills()); // Convert Set to List if needed

		return new ResponseEntity<>(skills, HttpStatus.OK);
	}

	// Get all tasks posted by the Organization
	@GetMapping("/organizations/{organizationId}/tasks")
	public ResponseEntity<List<Task>> getOrganizationTasks(@PathVariable Long organizationId) {
		Optional<Organization> organizationOpt = userService.findOrganizationById(organizationId);
		if (organizationOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Organization organization = organizationOpt.get();
		return new ResponseEntity<>(organization.getTasks(), HttpStatus.OK);
	}

	// Add task to Organization
	// POST or Create a new task
	// Request includes:
	// {
	//    "title": "Community Cleanup",
	//    "description": "Join us for a community cleanup event to beautify our local park.",
	//    "location": "Central Park, Main St.",
	//    "eventDate": "2023-12-15",  // Ensure this date is in the future
	// }
	@PostMapping("/organizations/{organizationId}/tasks")
	public ResponseEntity<Organization> addTaskToOrganization(@PathVariable Long organizationId, @RequestBody TaskDto taskDto) {
		Optional<Organization> organizationOpt = userService.findOrganizationById(organizationId);
		if (organizationOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// Check if applicationDeadline and cancellationDeadline are in the future
		if (taskDto.getApplicationDeadline().isBefore(LocalDate.now()) ||
				taskDto.getCancellationDeadline().isBefore(LocalDate.now())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		// Validate that applicationDeadline and cancellationDeadline are before eventDate
		if (taskDto.getApplicationDeadline().isAfter(taskDto.getEventDate()) ||
				taskDto.getCancellationDeadline().isAfter(taskDto.getEventDate())) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Organization organization = organizationOpt.get();
		Task task = new Task();
		try {
			task.setTitle(taskDto.getTitle());
			task.setDescription(taskDto.getDescription());
			task.setLocation(taskDto.getLocation());
			task.setEventDate(taskDto.getEventDate());
			task.setCancellationDeadline(taskDto.getCancellationDeadline());
			task.setApplicationDeadline(taskDto.getApplicationDeadline());
			taskService.saveTask(task);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		organization.getTasks().add(task);
		userService.saveUser(organization);
		return new ResponseEntity<>(organization, HttpStatus.OK);
	}

	// Update task in an organization
	@PutMapping("/organizations/{organizationId}/tasks/{taskId}")
	public ResponseEntity<Organization> updateTaskInOrganization(@PathVariable Long organizationId, @PathVariable Long taskId, @RequestBody TaskPartialDto updatedTask) {
		Optional<Organization> organizationOpt = userService.findOrganizationById(organizationId);
		if (organizationOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Organization organization = organizationOpt.get();
		Task existingTask = taskService.findById(taskId).orElse(null);
		if (existingTask == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		try {
			existingTask.setTitle(updatedTask.getTitle() != null ? updatedTask.getTitle() : existingTask.getTitle());
			existingTask.setDescription(updatedTask.getDescription() != null ? updatedTask.getDescription() : existingTask.getDescription());
			existingTask.setLocation(updatedTask.getLocation() != null ? updatedTask.getLocation() : existingTask.getLocation());
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		taskService.saveTask(existingTask);
		return new ResponseEntity<>(organization, HttpStatus.OK);
	}

	// Delete task permanently in an organization
	@DeleteMapping("/organizations/{organizationId}/tasks/{taskId}")
	public ResponseEntity<Organization> deleteTaskInOrganization(@PathVariable Long organizationId, @PathVariable Long taskId) {
		Optional<Organization> organizationOpt = userService.findOrganizationById(organizationId);
		if (organizationOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		Organization organization = organizationOpt.get();
		Task existingTask = taskService.findById(taskId).orElse(null);
		if (existingTask == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// Retrieve all signups for the task
		List<TaskSignup> signups = taskSignupService.getTaskSignups(taskId);

		// Delete all signups associated with the task
		for (TaskSignup signup : signups) {
			taskSignupService.deleteById(signup.getSignupId());
		}

		organization.getTasks().remove(existingTask);
		userService.saveUser(organization);
		taskService.deleteByTaskId(taskId); // Delete task completely. We do not want tasks without organization
		return new ResponseEntity<>(organization, HttpStatus.OK);
	}
}
