package com.example.volunteer_platform.service;

import com.example.volunteer_platform.dto.OrganizationDto;
import com.example.volunteer_platform.dto.VolunteerDto;
import com.example.volunteer_platform.model.Organization;
import com.example.volunteer_platform.model.Volunteer;
import com.example.volunteer_platform.repository.OrganizationRepository;
import com.example.volunteer_platform.repository.VolunteerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.volunteer_platform.model.User;
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

    /**
     * Register a new user.
     *
     * @param user User to be saved.
     */
    public void saveUser (User user) {
        userRepository.save(user);
    }

    @Transactional
    public void saveVolunteer (VolunteerDto volunteerDTO) {
        try {
            Volunteer volunteer = new Volunteer();
            volunteer.setName(volunteerDTO.getName());
            volunteer.setEmail(volunteerDTO.getEmail());
            volunteer.setPassword(volunteerDTO.getPassword());
            volunteer.setPhoneNumber(volunteerDTO.getPhoneNumber());
            volunteer.setGender(volunteerDTO.getGender());
            saveUser(volunteer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException("Could not create volunteer:" + e);
        }
    }

    @Transactional
    public void saveOrganization (OrganizationDto orgDTO) {
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
            System.out.println(e.getMessage());
            throw new RuntimeException("Could not create organization:" + e);
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
}