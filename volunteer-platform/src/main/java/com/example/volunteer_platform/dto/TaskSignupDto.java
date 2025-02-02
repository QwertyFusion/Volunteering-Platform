package com.example.volunteer_platform.dto;

import lombok.Builder;
import lombok.Getter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Data Transfer Object for task signup requests.
 */
@Getter
@Builder
public class TaskSignupDto {

    /**
     * ID of the volunteer signing up for the task.
     */
    @NotNull(message = "Volunteer ID cannot be null")
    @Positive(message = "Volunteer ID must be a positive number")
    private final Long volunteerId;

    /**
     * ID of the task being signed up for.
     */
    @NotNull(message = "Task ID cannot be null")
    @Positive(message = "Task ID must be a positive number")
    private final Long taskId;

    // Lombok's @Builder will handle object creation, so constructor is not needed unless specifically required
}
