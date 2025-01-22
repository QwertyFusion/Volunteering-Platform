package com.example.volunteer_platform.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.volunteer_platform.dto.LoginDto;
import com.example.volunteer_platform.dto.OrganizationDto;
import com.example.volunteer_platform.dto.UserDto;
import com.example.volunteer_platform.dto.VolunteerDto;
import com.example.volunteer_platform.model.Organization;
import com.example.volunteer_platform.model.User;
import com.example.volunteer_platform.model.Volunteer;
import com.example.volunteer_platform.repository.OrganizationRepository;
import com.example.volunteer_platform.repository.UserRepository;
import com.example.volunteer_platform.repository.VolunteerRepository;

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
    public UserService(UserRepository userRepository, BCryptPasswordEncoder PasswordEncoder) {
        this.userRepository = userRepository;
        this.PasswordEncoder = PasswordEncoder;
    }
   
    /**
     * Create and save a new user based on the provided userDto (with Lombok Builder).
     */
    public User createUser(UserDto userDto) {
        User user = User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .password(PasswordEncoder.encode(userDto.getPassword())) // Encrypting password
                .phoneNumber(userDto.getPhoneNumber())
                .build();

        return userRepository.save(user);
    }

    /**
     * Register a new volunteer.
     *
     * @param volunteerDto DTO containing volunteer details.
     */
    public void saveVolunteer(VolunteerDto volunteerDto) {
        Volunteer volunteer = new Volunteer();
        volunteer.setName(volunteerDto.getName());
        volunteer.setEmail(volunteerDto.getEmail());
        volunteer.setPassword(PasswordEncoder.encode(volunteerDto.getPassword())); // Encrypt the password
        volunteer.setPhoneNumber(volunteerDto.getPhoneNumber());
        volunteer.setRole("VOLUNTEER");

        // Save the volunteer
        volunteerRepository.save(volunteer);
    }

    /**
     * Register a new organization.
     *
     * @param organizationDto DTO containing organization details.
     */
    public void saveOrganization(OrganizationDto organizationDto) {
        Organization organization = new Organization();
        organization.setName(organizationDto.getName());
        organization.setEmail(organizationDto.getEmail());
        organization.setPassword(PasswordEncoder.encode(organizationDto.getPassword())); // Encrypt the password
        organization.setPhoneNumber(organizationDto.getPhoneNumber());
        organization.setRole("ORGANIZATION");

        // Save the organization
        organizationRepository.save(organization);
    }

    /**
     * Get all users in the system.
     *
     * @return List of all users.
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
     * @return List of all organizations.
     */
    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    /**
     * Get all volunteers in the system.
     *
     * @return List of all volunteers.
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
     * Method to authenticate volunteer login.
     *
     * @param loginDto DTO containing login details for volunteers.
     * @return Optional containing the matching volunteer or empty if not found.
     */
    public Optional<Volunteer> authenticateVolunteer(LoginDto loginDto) {
        return volunteerRepository.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
    }

    /**
     * Method to authenticate organization login.
     *
     * @param loginDto DTO containing login details for organizations.
     * @return Optional containing the matching organization or empty if not found.
     */
    public Optional<Organization> authenticateOrganization(LoginDto loginDto) {
        return organizationRepository.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
    }

}
