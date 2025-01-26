package com.example.volunteer_platform.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String redirectUrl;

        // Check the roles of the authenticated user
        if (authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ORGANIZATION"))) {
            redirectUrl = "/o/current_tasks"; // Redirect to current tasks for organizations
        } else if (authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_VOLUNTEER"))) {
            redirectUrl = "/v/opportunities"; // Redirect to opportunities for volunteers
        } else {
            redirectUrl = "/home"; // Default redirect if no role matches
        }

        response.sendRedirect(redirectUrl);
    }
}