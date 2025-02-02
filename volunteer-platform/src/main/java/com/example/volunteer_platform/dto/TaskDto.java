package com.example.volunteer_platform.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for the Task entity, including all required fields for task creation.
 */
@Data
public class TaskDto {

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    @Size(max = 100)
    private String location;

    @NotNull(message = "Skills cannot be null")
    private List<String> skills; // Add this field

    @NotNull
    @Future(message = "Event date must be in the future")
    private LocalDate eventDate; // When the event will be hosted

    /**
     * Cancellation deadline for the task signup.
     * Must be a valid future date.
     */
    @NotNull(message = "Cancellation deadline cannot be null")
    @Future(message = "Cancellation deadline must be a future date")
    private  LocalDate cancellationDeadline;

    /**
     * Application deadline for the task signup.
     * Must be a valid future date.
     */
    @NotNull(message = "Application deadline cannot be null")
    @Future(message = "Application deadline must be a future date")
    private  LocalDate applicationDeadline;
}