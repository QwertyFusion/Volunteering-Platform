package com.example.volunteer_platform.controller;

import com.example.volunteer_platform.dto.RatingsDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.volunteer_platform.model.Ratings;
import com.example.volunteer_platform.service.RatingsService;

import java.util.List;
import java.util.Optional;

/**
 * RatingsController handles API endpoints for managing ratings between volunteers and organizations.
 */
@RestController
@RequestMapping("/api/ratings")
public class RatingsController {

    @Autowired
    private RatingsService ratingsService;

    /**
     * Get all ratings for a specific user.
     *
     * @param ratedUserId The ID of the user being rated.
     * @return List of ratings for the user.
     */
    @GetMapping("/forUser/{ratedUserId}")
    public ResponseEntity<List<Ratings>> getRatingsForUser(@PathVariable long ratedUserId) {
        List<Ratings> ratings = ratingsService.getRatingsForUser(ratedUserId);
        if (ratings == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }

    /**
     * Get all ratings submitted by a specific user.
     *
     * @param ratedByUserId The ID of the user who submitted the ratings.
     * @return List of ratings submitted by the user.
     */
    @GetMapping("/byUser/{ratedByUserId}")
    public ResponseEntity<List<Ratings>> getRatingsByUser (@PathVariable int ratedByUserId) {
        List<Ratings> ratings = ratingsService.getRatingsByUser(ratedByUserId);
        if (ratings == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }

    /**
     * Get a specific rating by its ID.
     *
     * @param ratingId The ID of the rating.
     * @return The rating details.
     */
    @GetMapping("/{ratingId}")
    public ResponseEntity<Ratings> getRatingById(@PathVariable int ratingId) {
        Optional<Ratings> ratingOpt = ratingsService.getRatingById(ratingId);
        if (ratingOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Ratings rating = ratingOpt.get();
        return new ResponseEntity<>(rating, HttpStatus.OK);
    }

    /**
     * Submit a rating for an organization or volunteer.
     *
     * @param ratingDto The rating to be submitted.
     * @return The submitted rating.
     */
    @PostMapping
    @Transactional
    public ResponseEntity<Ratings> submitRating(@RequestBody @Valid RatingsDto ratingDto) {
        Ratings rating = new Ratings();
        rating.setRatedByUserId(ratingDto.getRatedByUserId());
        rating.setRatedUserId(ratingDto.getRatedUserId());
        rating.setRatingScore(ratingDto.getRatingScore());
        rating.setReview(ratingDto.getReview());

        if (!ratingsService.canRate(rating)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Invalid rating submission
        }

        Ratings submittedRating = ratingsService.submitRating(rating);
        return new ResponseEntity<>(submittedRating, HttpStatus.OK);
    }

    /**
     * Edit an existing rating.
     *
     * @param ratingId The ID of the rating to be edited.
     * @param ratingDto The updated rating details.
     * @return The updated rating.
     */
    @PutMapping("/{ratingId}")
    @Transactional
    public ResponseEntity<Ratings> editRating(@PathVariable long ratingId, @RequestBody @Valid RatingsDto ratingDto) {
        Optional<Ratings> optionalRating = ratingsService.getRatingById(ratingId);
        if (optionalRating.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Ratings existingRating = optionalRating.get();
        try {
            existingRating.setRatingScore(ratingDto.getRatingScore());
            existingRating.setReview(ratingDto.getReview());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ratingsService.submitRating(existingRating);
        return new ResponseEntity<>(existingRating, HttpStatus.OK);
    }

    /**
     * Delete a rating by its ID.
     *
     * @param ratingId The ID of the rating to be deleted.
     * @return HTTP 204 if deleted, HTTP 404 if not found.
     */
    @DeleteMapping("/{ratingId}")
    @Transactional
    public ResponseEntity<Void> deleteRating(@PathVariable int ratingId) {
        Optional<Ratings> rating = ratingsService.getRatingById(ratingId);
        if (rating.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ratingsService.deleteRating(ratingId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
