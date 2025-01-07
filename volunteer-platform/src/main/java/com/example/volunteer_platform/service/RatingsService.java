package com.example.volunteer_platform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.volunteer_platform.model.Ratings;
import com.example.volunteer_platform.repository.RatingsRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * RatingsService provides methods to manage ratings between volunteers and organizations.
 */
@Service
public class RatingsService {

    @Autowired
    private RatingsRepository ratingsRepository;

    /**
     * Submit a rating after validation.
     *
     * @param rating The rating to be submitted.
     * @return The submitted rating.
     */
    public Ratings submitRating(Ratings rating) {
        return ratingsRepository.save(rating);
    }

    /**
     * Check if a rating can be submitted based on the event date.
     *
     * @param rating The rating to be checked.
     * @return True if the rating can be submitted, false otherwise.
     */
    public boolean canRate(Ratings rating) {
        // Logic to check if the event date has passed
        // This requires access to the task associated with the rating
        // Assuming we have a method to get the task's event date
        // LocalDate eventDate = getEventDateForRating(rating);
        // return eventDate != null && LocalDate.now().isAfter(eventDate);
        return true; // placeholder value
    }

    /**
     * Get all ratings for a specific user.
     *
     * @param ratedUserId The ID of the user being rated.
     * @return List of ratings for the user.
     */
    public List<Ratings> getRatingsForUser (long ratedUserId) {
        return ratingsRepository.findByRatedUserId(ratedUserId);
    }

    /**
     * Get all ratings submitted by a specific user.
     *
     * @param ratedByUserId The ID of the user who submitted the ratings.
     * @return List of ratings submitted by the user.
     */
    public List<Ratings> getRatingsByUser (long ratedByUserId) {
        return ratingsRepository.findByRatedByUserId(ratedByUserId);
    }

    /**
     * Get a specific rating by its ID.
     *
     * @param ratingId The ID of the rating.
     * @return The rating details.
     */
    public Optional<Ratings> getRatingById(long ratingId) {
        return ratingsRepository.findById(ratingId);
    }

    /**
     * Delete a rating by its ID.
     *
     * @param ratingId The ID of the rating to be deleted.
     */
    public void deleteRating(long ratingId) {
        ratingsRepository.deleteById(ratingId);
    }

    private LocalDate getEventDateForRating(Ratings rating) {
        // Implement logic to retrieve the event date based on the rating
        // This may involve querying the task associated with the rating
        return null; // placeholder value
    }
}