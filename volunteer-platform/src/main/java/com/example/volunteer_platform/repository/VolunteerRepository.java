package com.example.volunteer_platform.repository;

import com.example.volunteer_platform.model.Volunteer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Volunteer entities.
 */
@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    // Additional query methods can be defined here if needed
	//Volunteer findByEmail(String email);
	Optional<Volunteer> findByEmail(String email);  // Use Optional for safety
}