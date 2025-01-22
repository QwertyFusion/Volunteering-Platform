package com.example.volunteer_platform.service;

import com.example.volunteer_platform.dto.OrganizationDto;
import com.example.volunteer_platform.dto.OrganizationPartialDto;
import com.example.volunteer_platform.dto.VolunteerDto;
import com.example.volunteer_platform.dto.VolunteerPartialDto;
import com.example.volunteer_platform.model.*;
import com.example.volunteer_platform.repository.OrganizationRepository;
import com.example.volunteer_platform.repository.VolunteerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.volunteer_platform.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * UserService provides methods to manage users, including volunteers and organizations.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;
    @Autowired
    private TaskSignupService taskSignupService;
    @Autowired
    private TaskService taskService;

    /**
     * Register a new user.
     *
     * @param user User to be saved.
     */
    public void saveUser (User user) {
        userRepository.save(user);
    }

    /**
     * Save a new volunteer.
     *
     * @param volunteerDTO Data Transfer Object containing volunteer details.
     * @throws RuntimeException if there is an error during saving.
     */
    @Transactional
    public void saveVolunteer(VolunteerDto volunteerDTO) {
        try {
            Volunteer volunteer = new Volunteer();
            volunteer.setName(volunteerDTO.getName());
            volunteer.setEmail(volunteerDTO.getEmail());
            volunteer.setPassword(volunteerDTO.getPassword());
            volunteer.setPhoneNumber(volunteerDTO.getPhoneNumber());
            volunteer.setGender(volunteerDTO.getGender());
            saveUser(volunteer);
        } catch (Exception e) {
            System.out.println("Error saving volunteer: " + e.getMessage());
            throw new RuntimeException("Could not create volunteer: " + e.getMessage());
        }
    }

    /**
     * Save a new organization.
     *
     * @param orgDTO Data Transfer Object containing organization details.
     * @throws RuntimeException if there is an error during saving.
     */
    @Transactional
    public void saveOrganization(OrganizationDto orgDTO) {
        try {
            Organization org = new Organization();
            org.setName(orgDTO.getName());
            org.setEmail(orgDTO.getEmail());
            org.setPassword(orgDTO.getPassword());
            org.setPhoneNumber(orgDTO.getPhoneNumber());
            org.setAddress(orgDTO.getAddress());
            org.setWebsite(orgDTO.getWebsite());
            saveUser(org);
        } catch (Exception e) {
            System.out.println("Error saving organization: " + e.getMessage());
            throw new RuntimeException("Could not create organization: " + e.getMessage());
        }
    }

    /**
     * Get all users in the system.
     *
     * @return List of users.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Find a user by their email.
     *
     * @param email Email of the user.
     * @return User object if found, otherwise null.
     */
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Find a user by their ID.
     *
     * @param id User ID.
     * @return Optional containing the user if found.
     */
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Delete a user by their ID.
     *
     * @param id User ID.
     */
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    /**
     * Get all organizations in the system.
     *
     * @return List of organizations.
     */
    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    /**
     * Get all volunteers in the system.
     *
     * @return List of volunteers.
     */
    public List<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
    }

    /**
     * Find an organization by its ID.
     *
     * @param id Organization ID.
     * @return Optional containing the organization if found.
     */
    public Optional<Organization> findOrganizationById(Long id) {
        return organizationRepository.findById(id);
    }

    /**
     * Find a volunteer by their ID.
     *
     * @param id Volunteer ID.
     * @return Optional containing the volunteer if found.
     */
    public Optional<Volunteer> findVolunteerById(Long id) {
        return volunteerRepository.findById(id);
    }

    /**
     * Update an organization's details.
     *
     * @param organizationId ID of the organization to update.
     * @param updatedOrg Partial data for the organization update.
     * @return Optional containing the updated organization if successful, otherwise empty.
     * @throws RuntimeException if there is an error during the update.
     */
    @Transactional
    public Optional<Organization> updateOrganization(Long organizationId, OrganizationPartialDto updatedOrg) {
        try {
            Organization existingOrg = findOrganizationById(organizationId).orElse(null);
            if (existingOrg == null) {
                return Optional.empty();
            }

            existingOrg.setName(updatedOrg.getName() != null ? updatedOrg.getName() : existingOrg.getName());
            existingOrg.setEmail(updatedOrg.getEmail() != null ? updatedOrg.getEmail() : existingOrg.getEmail());
            existingOrg.setPassword(updatedOrg.getPassword() != null ? updatedOrg.getPassword() : existingOrg.getPassword());
            existingOrg.setPhoneNumber(updatedOrg.getPhoneNumber() != null ? updatedOrg.getPhoneNumber() : existingOrg.getPhoneNumber());
            existingOrg.setAddress(updatedOrg.getAddress() != null ? updatedOrg.getAddress() : existingOrg.getAddress());
            existingOrg.setWebsite(updatedOrg.getWebsite() != null ? updatedOrg.getWebsite() : existingOrg.getWebsite());

            saveUser (existingOrg);
            return Optional.of(existingOrg);
        } catch (Exception e) {
            System.out.println("Error updating organization: " + e.getMessage());
            throw new RuntimeException("Could not update organization: " + e.getMessage());
        }
    }

    /**
     * Delete an organization by its ID.
     *
     * @param organizationId ID of the organization to delete.
     * @return true if the organization was deleted, false if not found.
     * @throws RuntimeException if there is an error during deletion.
     */
    @Transactional
    public boolean deleteOrganizationById(Long organizationId) {
        try {
            Optional<Organization> organizationOpt = findOrganizationById(organizationId);
            if (organizationOpt.isEmpty()) {
                return false;
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

            deleteUserById(organizationId);
            return true;
        } catch (Exception e) {
            System.out.println("Error deleting organization: " + e.getMessage());
            throw new RuntimeException("Could not delete organization: " + e.getMessage());
        }
    }

    /**
     * Update a volunteer's details.
     *
     * @param volunteerId ID of the volunteer to update.
     * @param updatedVol Partial data for the volunteer update.
     * @return Optional containing the updated volunteer if successful, otherwise empty.
     * @throws RuntimeException if there is an error during the update.
     */
    @Transactional
    public Optional<Volunteer> updateVolunteer(Long volunteerId, VolunteerPartialDto updatedVol) {
        try {
            Volunteer existingVol = findVolunteerById(volunteerId).orElse(null);
            if (existingVol == null) {
                return Optional.empty();
            }

            existingVol.setName(updatedVol.getName() != null ? updatedVol.getName() : existingVol.getName());
            existingVol.setEmail(updatedVol.getEmail() != null ? updatedVol.getEmail() : existingVol.getEmail());
            existingVol.setPassword(updatedVol.getPassword() != null ? updatedVol.getPassword() : existingVol.getPassword());
            existingVol.setPhoneNumber(updatedVol.getPhoneNumber() != null ? updatedVol.getPhoneNumber() : existingVol.getPhoneNumber());

            saveUser (existingVol);
            return Optional.of(existingVol);
        } catch (Exception e) {
            System.out.println("Error updating volunteer: " + e.getMessage());
            throw new RuntimeException("Could not update volunteer: " + e.getMessage());
        }
    }

    /**
     * Delete a volunteer by their ID.
     *
     * @param volunteerId ID of the volunteer to delete.
     * @return true if the volunteer was deleted, false if not found.
     * @throws RuntimeException if there is an error during deletion.
     */
    @Transactional
    public boolean deleteVolunteerById(Long volunteerId) {
        try {
            Optional<Volunteer> volunteerOpt = findVolunteerById(volunteerId);
            if (volunteerOpt.isEmpty()) {
                return false;
            }

            List<TaskSignup> signups = taskSignupService.getUserSignups(volunteerId);

            for (TaskSignup signup : signups) {
                taskSignupService.deleteById(signup.getSignupId());
            }

            deleteUserById(volunteerId);
            return true;
        } catch (Exception e) {
            System.out.println("Error deleting volunteer: " + e.getMessage());
            throw new RuntimeException("Could not delete volunteer: " + e.getMessage());
        }
    }
}