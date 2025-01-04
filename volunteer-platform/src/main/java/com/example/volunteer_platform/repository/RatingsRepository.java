package com.example.volunteer_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.volunteer_platform.model.Ratings;

import java.util.List;

@Repository
public interface RatingsRepository extends JpaRepository<Ratings, Integer> {
    List<Ratings> findByRatedUserId(int ratedUserId);
    List<Ratings> findByRatedByUserId(int ratedByUserId);
}
