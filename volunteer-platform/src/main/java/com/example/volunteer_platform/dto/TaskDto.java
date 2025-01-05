package com.example.volunteer_platform.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

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

    @NotNull
    @Future
    private LocalDate eventDate; // When the event will be hosted

    /**
     * Cancellation deadline for the task signup.
     * Must be a valid future date.
     */
    @NotNull(message = "Cancellation deadline cannot be null")
    @Future(message = "Cancellation deadline must be a future date")
    private final LocalDate cancellationDeadline;

    /**
     * Application deadline for the task signup.
     * Must be a valid future date.
     */
    @NotNull(message = "Application deadline cannot be null")
    @Future(message = "Application deadline must be a future date")
    private final LocalDate applicationDeadline;
}