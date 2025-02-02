package com.example.volunteer_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.volunteer_platform.model.User;

/**
 * Repository interface for managing User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Find a user by their email.
	 *
	 * @param email Email of the user.
	 * @return User object if found.
	 */
	User findByEmail(String email);

}