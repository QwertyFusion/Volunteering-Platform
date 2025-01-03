package com.example.volunteer_platform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@DiscriminatorValue("ORGANIZATION")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organization extends User {

    @NotBlank
    @Size(max = 255)
    private String address;

    @NotBlank
    @Size(max = 255)
    private String website;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks;
}