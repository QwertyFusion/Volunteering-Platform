package com.example.volunteer_platform.model;

import java.util.List;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Organization class represents an organization user in the system, extending the User class.
 */
@Entity
@DiscriminatorValue("ORGANIZATION")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Organization extends User {

    @Column(nullable = false)
    private String address; // Address of the organization

    @Column(nullable = false)
    private String website; // Website of the organization

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Task> tasks; // List of tasks created by the organization
}