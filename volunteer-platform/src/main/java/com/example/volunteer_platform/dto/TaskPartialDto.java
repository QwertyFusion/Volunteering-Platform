package com.example.volunteer_platform.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO for partial updates to the Task entity.
 */
@Data
public class TaskPartialDto {
    @Size(max = 100)
    private String title;

    private String description;

    @Size(max = 100)
    private String location;

    @Future(message = "Event date must be in the future")
    private LocalDate eventDate; // When the event will be hosted

    @Future(message = "Cancellation deadline must be a future date")
    private  LocalDate cancellationDeadline;

    @Future(message = "Application deadline must be a future date")
    private  LocalDate applicationDeadline;
}