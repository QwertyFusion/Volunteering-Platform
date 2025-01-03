package com.example.volunteer_platform.controller;

import com.example.volunteer_platform.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    // Test endpoint to send an email
    @GetMapping("/send-test-email")
    public String sendTestEmail() {
        // Send a test email using EmailService
        emailService.sendEmail("21jr1a0594@gmail.com", "Test Subject", "This is a test email.");
        return "Test email sent successfully!";
    }
}
