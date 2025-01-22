package com.example.volunteer_platform.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User class represents a user in the system, which can be a volunteer or an organization.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Size(min = 8)
    @Column(nullable = false)
    private String password; // Ensure this is hashed before saving

    @NotBlank
    @Pattern(regexp = "\\+?\\d{10,15}", message = "Invalid phone number")
    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // Use ENUM for role

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Custom Getter to integrate with Spring Security (with "ROLE_" prefix)
    public String getRoleWithPrefix() {
        return "ROLE_" + role.name();
    }

    // Custom setter to handle role from string input
    public void setRoleFromString(String roleString) {
        try {
            this.role = Role.valueOf(roleString.toUpperCase());  // Convert string to enum value, ensuring it's uppercase
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + roleString);
        }
    }

    // Role Enum
    public enum Role {
        VOLUNTEER, ORGANIZATION
    }
}
