package com.example.volunteer_platform.dto;

import java.util.Set;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for partial updates to the Volunteer entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerPartialDto {
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    private String email;

    @Size(min = 8, message = "Password must have at least 8 characters")
    private String password;

    @Size(max = 15, message = "Phone number cannot exceed 15 characters")
    private String phoneNumber;
    
   private Set<Long> skillIds;
}