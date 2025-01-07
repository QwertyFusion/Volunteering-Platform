package com.example.volunteer_platform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

/**
 * Ratings class represents the ratings given by volunteers to organizations and vice versa.
 */
@Entity
@Table(name = "ratings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ratings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ratingId;

    @Column(nullable = false)
    private long ratedByUserId; // ID of the user who rated

    @Column(nullable = false)
    private long ratedUserId; // ID of the user being rated

    @Column(nullable = false)
    private int ratingScore; // Rating score (e.g., 1-5)

    @Column(columnDefinition = "TEXT")
    private String review; // Review text

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate; // Date when the rating was created

    @Column(nullable = false)
    private LocalDateTime updatedDate; // Date when the rating was last updated

    @Column(nullable = false)
    private boolean isDeleted; // Flag to indicate if the user has been deleted

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
        isDeleted = false; // Default value
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }
}