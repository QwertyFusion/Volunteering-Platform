package com.example.volunteer_platform.service;

import com.example.volunteer_platform.model.Organization;
import com.example.volunteer_platform.model.Volunteer;
import com.example.volunteer_platform.repository.OrganizationRepository;
import com.example.volunteer_platform.repository.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.volunteer_platform.model.User;
import com.example.volunteer_platform.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    // Register User
    public void saveUser(User user) {
        userRepository.save(user);
    }

    // Get all Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Find User By Email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Find User By ID
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    // Delete User By ID
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    // Get all Organizations
    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    // Get all Volunteers
    public List<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
    }

    // Find Organization By ID
    public Optional<Organization> findOrganizationById(Long id) {
        return organizationRepository.findById(id);
    }

    // Find Volunteer By ID
    public Optional<Volunteer> findVolunteerById(Long id) {
        return volunteerRepository.findById(id);
    }
}
