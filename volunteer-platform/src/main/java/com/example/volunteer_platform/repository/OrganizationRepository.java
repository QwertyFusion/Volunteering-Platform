package com.example.volunteer_platform.repository;

import com.example.volunteer_platform.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Organization entities.
 */
@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    // Additional query methods can be defined here if needed
}