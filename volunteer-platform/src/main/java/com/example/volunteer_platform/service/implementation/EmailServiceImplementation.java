package com.example.volunteer_platform.service.implementation;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.example.volunteer_platform.service.EmailService;

/**
 * EmailServiceImplementation provides methods to send emails in the system.
 * This is an implementation of the EmailService interface.
 */
@Service
@RequiredArgsConstructor
public class EmailServiceImplementation implements EmailService {
    private final JavaMailSender mailSender;

    @Override
    public void sendReminderEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("Email sent successfully to: " + to); // Add this for debug
        } catch (Exception e) {
            System.out.println("Failed to send email to: " + to);
            e.printStackTrace();
        }
    }
}