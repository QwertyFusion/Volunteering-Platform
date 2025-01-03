//package com.example.volunteer_platform.service;
//
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//import jakarta.mail.internet.MimeMessage;
//
//@Service
//public class EmailService {
//
//    private final JavaMailSender javaMailSender;
//
//    public EmailService(JavaMailSender javaMailSender) {
//        this.javaMailSender = javaMailSender;
//    }
//
//    public void sendEmail(String to, String subject, String text) {
//        try {
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, false); // false for plain text
//            
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(text); // Plain text content
//            
//            javaMailSender.send(message);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//}
