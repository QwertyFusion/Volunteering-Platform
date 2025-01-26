package com.example.volunteer_platform.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Store the username in the session
        String username = authentication.getName(); // Get the username
        request.getSession().setAttribute("user", username); // Store username in session

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst() // Get the first role
                .orElse(null); // Default to null if no role found
        request.getSession().setAttribute("role", role); // Store role in session

        String redirectUrl;

        // Check the roles of the authenticated user
        if ("ROLE_ORGANIZATION".equals(role)) {
            redirectUrl = "/o/current_tasks"; // Redirect to current tasks for organizations
        } else if ("ROLE_VOLUNTEER".equals(role)) {
            redirectUrl = "/v/opportunities"; // Redirect to opportunities for volunteers
        } else {
            redirectUrl = "/home"; // Default redirect if no role matches
        }

        response.sendRedirect(redirectUrl);
    }
}