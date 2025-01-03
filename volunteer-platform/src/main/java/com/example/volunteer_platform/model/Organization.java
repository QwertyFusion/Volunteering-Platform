package com.example.volunteer_platform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("ORGANIZATION")
@Data
@NoArgsConstructor
public class Organization extends User {

    @NotBlank
    @Size(max = 255)
    private String address;

    @NotBlank
    @Size(max = 255)
    private String website;

    // Methods for creating, updating, and deleting tasks can be added here
}