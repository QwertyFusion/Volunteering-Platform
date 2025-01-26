package com.example.volunteer_platform.security;

import com.example.volunteer_platform.model.Organization;
import com.example.volunteer_platform.model.Skill;
import com.example.volunteer_platform.model.User;
import com.example.volunteer_platform.model.Volunteer;
import com.example.volunteer_platform.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    public CustomAuthenticationSuccessHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Store the username in the session
        String username = authentication.getName(); // Get the username (email)
        request.getSession().setAttribute("user", username); // Store username(email) in session

        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst() // Get the first role
                .orElse(null); // Default to null if no role found
        request.getSession().setAttribute("role", role); // Store role in session

        // Fetch the user details from the database
        Long id = userService.findByEmail(username).getId();
        if ("ROLE_ORGANIZATION".equals(role) && id != null) {
            Optional<Organization> userObj = userService.findOrganizationById(id);
            if (userObj.isPresent()) {
                request.getSession().setAttribute("userId", userObj.get().getId());
                request.getSession().setAttribute("name", userObj.get().getName());
                request.getSession().setAttribute("email", userObj.get().getEmail());
                request.getSession().setAttribute("phone", userObj.get().getPhoneNumber());
                request.getSession().setAttribute("address", userObj.get().getAddress());
                request.getSession().setAttribute("website", userObj.get().getWebsite());
                request.getSession().setAttribute("gender", null);
                request.getSession().setAttribute("skills", null);
                request.getSession().setAttribute("skillNames", null);
                request.getSession().setAttribute("tasks", userObj.get().getTasks());
                request.getSession().setAttribute("createdAt", userObj.get().getCreatedAt());
                request.getSession().setAttribute("updatedAt", userObj.get().getUpdatedAt());
            }
        }
        else if ("ROLE_VOLUNTEER".equals(role) && id != null) {
            Optional<Volunteer> userObj = userService.findVolunteerById(id);
            if (userObj.isPresent()) {
                request.getSession().setAttribute("userId", userObj.get().getId());
                request.getSession().setAttribute("name", userObj.get().getName());
                request.getSession().setAttribute("email", userObj.get().getEmail());
                request.getSession().setAttribute("phone", userObj.get().getPhoneNumber());
                request.getSession().setAttribute("address", null);
                request.getSession().setAttribute("website", null);
                request.getSession().setAttribute("gender", userObj.get().getGender());
                request.getSession().setAttribute("skills", userObj.get().getSkills());
                String skillNames = userObj.get().getSkills().stream()
                        .map(Skill::getName)
                        .collect(Collectors.joining(","));
                request.getSession().setAttribute("skillNames", skillNames);

                request.getSession().setAttribute("createdAt", userObj.get().getCreatedAt());
                request.getSession().setAttribute("updatedAt", userObj.get().getUpdatedAt());
            }
        }

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