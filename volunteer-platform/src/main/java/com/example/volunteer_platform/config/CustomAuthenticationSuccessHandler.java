package com.example.volunteer_platform.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // Check user roles and redirect accordingly
        if (!CollectionUtils.isEmpty(authentication.getAuthorities())) {
            for (var authority : authentication.getAuthorities()) {
                if (authority.equals(new SimpleGrantedAuthority("ROLE_ORGANIZATION"))) {
                    response.sendRedirect("/organization/dashboard"); // Redirect to organization dashboard
                    return;
                } else if (authority.equals(new SimpleGrantedAuthority("ROLE_VOLUNTEER"))) {
                    response.sendRedirect("/volunteer/dashboard"); // Redirect to volunteer dashboard
                    return;
                }
            }
        }
        // Default redirection in case of some issue
        response.sendRedirect("/login?error");
    }
}
