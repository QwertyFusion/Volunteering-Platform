package com.example.volunteer_platform.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.volunteer_platform.model.User;
import com.example.volunteer_platform.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);  // Return null if not found
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    public User saveUser(User user) {
        return userRepository.save(user);  // Save the user to the database
    }
}
