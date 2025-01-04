package com.example.volunteer_platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.volunteer_platform.model.Ratings;
import com.example.volunteer_platform.repository.RatingsRepository;

import java.util.List;

@Service
public class RatingsService {

    @Autowired
    private RatingsRepository ratingsRepository;

    public Ratings submitRating(Ratings rating) {
        // Add any validation or business logic here, e.g. verifying participation
        return ratingsRepository.save(rating);
    }

    public List<Ratings> getRatingsForUser(int ratedUserId) {
        return ratingsRepository.findByRatedUserId(ratedUserId);
    }

    public List<Ratings> getRatingsByUser(int ratedByUserId) {
        return ratingsRepository.findByRatedByUserId(ratedByUserId);
    }
}
