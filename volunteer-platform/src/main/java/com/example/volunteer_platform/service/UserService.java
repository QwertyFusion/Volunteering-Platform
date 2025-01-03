package com.example.volunteer_platform.service;

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
}
