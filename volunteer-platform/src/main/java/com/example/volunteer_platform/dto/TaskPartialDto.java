package com.example.volunteer_platform.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskPartialDto {
    @Size(max = 100)
    private String title;

    private String description;

    @Size(max = 100)
    private String location;

    @Future
    private LocalDate eventDate; // When the event will be hosted
}