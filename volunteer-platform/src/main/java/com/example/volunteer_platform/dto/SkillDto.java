package com.example.volunteer_platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Skill entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillDto {
    @NotBlank
    @Size(max = 100)
    private String name;
}