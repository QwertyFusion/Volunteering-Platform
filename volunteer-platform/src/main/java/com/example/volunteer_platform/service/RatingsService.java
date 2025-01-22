package com.example.volunteer_platform.service;

import com.example.volunteer_platform.model.Ratings;

import java.util.List;
import java.util.Optional;

/**
 * RatingsService provides methods to manage ratings between volunteers and organizations.
 */
public interface RatingsService {

    /**
     * Submit a rating after validation.
     *
     * @param rating The rating to be submitted.
     * @return The submitted rating.
     */
    Ratings submitRating(Ratings rating);

    /**
     * Check if a rating can be submitted based on the event date.
     *
     * @param rating The rating to be checked.
     * @return True if the rating can be submitted, false otherwise.
     */
    boolean canRate(Ratings rating);

    /**
     * Get all ratings for a specific user.
     *
     * @param ratedUserId The ID of the user being rated.
     * @return List of ratings for the user.
     */
    List<Ratings> getRatingsForUser (long ratedUserId);

    /**
     * Get all ratings submitted by a specific user.
     *
     * @param ratedByUserId The ID of the user who submitted the ratings.
     * @return List of ratings submitted by the user.
     */
    List<Ratings> getRatingsByUser (long ratedByUserId);

    /**
     * Get a specific rating by its ID.
     *
     * @param ratingId The ID of the rating.
     * @return The rating details.
     */
    Optional<Ratings> getRatingById(long ratingId);

    /**
     * Delete a rating by its ID.
     *
     * @param ratingId The ID of the rating to be deleted.
     */
    void deleteRating(long ratingId);
}