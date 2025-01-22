package com.example.volunteer_platform.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.volunteer_platform.model.Ratings;
import com.example.volunteer_platform.repository.RatingsRepository;
import com.example.volunteer_platform.service.RatingsService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * RatingsServiceImplementation provides methods to manage ratings between volunteers and organizations.
 * This is an implementation of the RatingsService interface.
 */
@Service
public class RatingsServiceImplementation implements RatingsService {

    @Autowired
    private RatingsRepository ratingsRepository;

    @Override
    public Ratings submitRating(Ratings rating) {
        return ratingsRepository.save(rating);
    }

    @Override
    public boolean canRate(Ratings rating) {
        // Logic to check if the event date has passed
        // This requires access to the task associated with the rating
        // Assuming we have a method to get the task's event date
        // LocalDate eventDate = getEventDateForRating(rating);
        // return eventDate != null && LocalDate.now().isAfter(eventDate);
        return true; // placeholder value
    }

    @Override
    public List<Ratings> getRatingsForUser (long ratedUserId) {
        return ratingsRepository.findByRatedUserId(ratedUserId);
    }

    @Override
    public List<Ratings> getRatingsByUser (long ratedByUserId) {
        return ratingsRepository.findByRatedByUserId(ratedByUserId);
    }

    @Override
    public Optional<Ratings> getRatingById(long ratingId) {
        return ratingsRepository.findById(ratingId);
    }

    @Override
    public void deleteRating(long ratingId) {
        ratingsRepository.deleteById(ratingId);
    }

    private LocalDate getEventDateForRating(Ratings rating) {
        // Implement logic to retrieve the event date based on the rating
        // This may involve querying the task associated with the rating
        return null; // placeholder value
    }
}