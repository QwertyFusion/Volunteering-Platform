package com.example.volunteer_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.volunteer_platform.model.Ratings;

import java.util.List;

/**
 * RatingsRepository provides methods to interact with the Ratings database.
 */
@Repository
public interface RatingsRepository extends JpaRepository<Ratings, Long> {
    List<Ratings> findByRatedUserId(long ratedUserId);
    List<Ratings> findByRatedByUserId(long ratedByUserId);
}