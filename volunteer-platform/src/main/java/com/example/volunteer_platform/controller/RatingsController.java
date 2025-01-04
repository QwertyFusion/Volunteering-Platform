package com.example.volunteer_platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.volunteer_platform.model.Ratings;
import com.example.volunteer_platform.service.RatingsService;

import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingsController {

    @Autowired
    private RatingsService ratingsService;

    @PostMapping("/submit")
    public ResponseEntity<Ratings> submitRating(@RequestBody Ratings rating) {
        Ratings submittedRating = ratingsService.submitRating(rating);
        return ResponseEntity.ok(submittedRating);
    }

    @GetMapping("/forUser/{ratedUserId}")
    public ResponseEntity<List<Ratings>> getRatingsForUser(@PathVariable int ratedUserId) {
        List<Ratings> ratings = ratingsService.getRatingsForUser(ratedUserId);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/byUser/{ratedByUserId}")
    public ResponseEntity<List<Ratings>> getRatingsByUser(@PathVariable int ratedByUserId) {
        List<Ratings> ratings = ratingsService.getRatingsByUser(ratedByUserId);
        return ResponseEntity.ok(ratings);
    }
}
