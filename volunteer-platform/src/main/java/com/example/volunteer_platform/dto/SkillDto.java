package com.example.volunteer_platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for the Skill entity, representing a skill that can be associated with a volunteer.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillDto {
    @NotBlank
    @Size(max = 100, message = "Skill name cannot exceed 100 characters")
    private String name; // Name of the skill
}