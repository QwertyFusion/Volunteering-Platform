package com.example.volunteer_platform.service;

import com.example.volunteer_platform.dto.OrganizationDto;
import com.example.volunteer_platform.dto.OrganizationPartialDto;
import com.example.volunteer_platform.dto.VolunteerDto;
import com.example.volunteer_platform.dto.VolunteerPartialDto;
import com.example.volunteer_platform.model.*;

import java.util.List;
import java.util.Optional;

/**
 * UserService provides methods to manage users, including volunteers and organizations.
 */
public interface UserService {

    /**
     * Register a new user.
     *
     * @param user User to be saved.
     */
    void saveUser(User user);

    /**
     * Save a new volunteer.
     *
     * @param volunteerDTO Data Transfer Object containing volunteer details.
     * @throws RuntimeException if there is an error during saving.
     */
    void saveVolunteer(VolunteerDto volunteerDTO);

    /**
     * Save a new organization.
     *
     * @param orgDTO Data Transfer Object containing organization details.
     * @throws RuntimeException if there is an error during saving.
     */
    void saveOrganization(OrganizationDto orgDTO);

    /**
     * Get all users in the system.
     *
     * @return List of users.
     */
    List<User> getAllUsers();
    
    

    
    //Optional<Volunteer> findByEmail(String email); 
    /**
     * Find a user by their email.
     *
     * @param email Email of the user.
     * @return User object if found, otherwise null.
     */
    User findByEmail(String email);

    /**
     * Find a user by their ID.
     *
     * @param id User ID.
     * @return Optional containing the user if found.
     */
    Optional<User> findUserById(Long id);

    /**
     * Delete a user by their ID.
     *
     * @param id User ID.
     */
    void deleteUserById(Long id);

    /**
     * Get all organizations in the system.
     *
     * @return List of organizations.
     */
    List<Organization> getAllOrganizations();

    /**
     * Get all volunteers in the system.
     *
     * @return List of volunteers.
     */
    List<Volunteer> getAllVolunteers();

    /**
     * Find an organization by its ID.
     *
     * @param id Organization ID.
     * @return Optional containing the organization if found.
     */
    Optional<Organization> findOrganizationById(Long id);

    /**
     * Find a volunteer by their ID.
     *
     * @param id Volunteer ID.
     * @return Optional containing the volunteer if found.
     */
    Optional<Volunteer> findVolunteerById(Long id);

    /**
     * Update an organization's details.
     *
     * @param organizationId ID of the organization to update.
     * @param updatedOrg Partial data for the organization update.
     * @return Optional containing the updated organization if successful, otherwise empty.
     * @throws RuntimeException if there is an error during the update.
     */
    Optional<Organization> updateOrganization(Long organizationId, OrganizationPartialDto updatedOrg);

    /**
     * Delete an organization by its ID.
     *
     * @param organizationId ID of the organization to delete.
     * @return true if the organization was deleted, false if not found.
     * @throws RuntimeException if there is an error during deletion.
     */
    boolean deleteOrganizationById(Long organizationId);

    /**
     * Update a volunteer's details.
     *
     * @param volunteerId ID of the volunteer to update.
     * @param updatedVol Partial data for the volunteer update.
     * @return Optional containing the updated volunteer if successful, otherwise empty.
     * @throws RuntimeException if there is an error during the update.
     */
    Optional<Volunteer> updateVolunteer(Long volunteerId, VolunteerPartialDto updatedVol);
    
    
   // List<Task> getVolunteeringHistory(Long volunteerId);

    /**
     * Delete a volunteer by their ID.
     *
     * @param volunteerId ID of the volunteer to delete.
     * @return true if the volunteer was deleted, false if not found.
     * @throws RuntimeException if there is an error during deletion.
     */
    boolean deleteVolunteerById(Long volunteerId);

	Optional<Volunteer> findVolunteerByEmailOptional(String email);
}