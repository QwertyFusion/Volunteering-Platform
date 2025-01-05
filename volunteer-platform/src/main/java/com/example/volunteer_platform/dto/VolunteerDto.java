package com.example.volunteer_platform.dto;

import com.example.volunteer_platform.enums.Gender;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for the Volunteer entity, including gender information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VolunteerDto extends UserDto {
    @NotNull(message = "Gender is required")
    private Gender gender; // Gender of the volunteer
}