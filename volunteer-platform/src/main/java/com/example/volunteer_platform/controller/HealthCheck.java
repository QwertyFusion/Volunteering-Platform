package com.example.volunteer_platform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HealthCheck controller provides an endpoint to check the health status of the application.
 */
@RestController
public class HealthCheck {
    /**
     * Health check endpoint.
     *
     * @return ResponseEntity with a status message indicating the application is running.
     */
    @GetMapping("/health-check")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }
}