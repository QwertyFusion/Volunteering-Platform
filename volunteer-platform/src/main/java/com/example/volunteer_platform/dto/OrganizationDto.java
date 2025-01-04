package com.example.volunteer_platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Organization entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDto extends UserDto {
    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    @NotBlank(message = "Website is required")
    @Size(max = 255, message = "Website cannot exceed 255 characters")
    private String website;
}
