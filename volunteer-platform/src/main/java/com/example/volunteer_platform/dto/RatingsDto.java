package com.example.volunteer_platform.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO for the Ratings entity, including all required fields for rating submission.
 */
@Data
public class RatingsDto {

    @NotNull(message = "Rated by user ID cannot be null")
    private Long ratedByUserId; // ID of the user who rated

    @NotNull(message = "Rated user ID cannot be null")
    private Long ratedUserId; // ID of the user being rated

    @NotNull(message = "Rating score cannot be null")
    @Min(value = 1, message = "Rating score must be at least 1")
    @Max(value = 5, message = "Rating score must be at most 5")
    private Integer ratingScore; // Rating score (e.g., 1-5)

    @Size(max = 500, message = "Review text cannot exceed 500 characters")
    private String review; // Review text
}