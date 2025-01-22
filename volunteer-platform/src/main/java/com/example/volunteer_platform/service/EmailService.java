package com.example.volunteer_platform.service;

/**
 * EmailService provides methods to send emails in the system.
 */
public interface EmailService {

    /**
     * Send a reminder email.
     *
     * @param to Recipient's email address.
     * @param subject Subject of the email.
     * @param body Body of the email.
     */
    void sendReminderEmail(String to, String subject, String body);
}