package com.example.volunteer_platform.controller.views;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class ViewsController {

    @GetMapping({"/", "/home"})
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    /**
     * Display the login page.
     *
     * @return The name of the login view.
     */
    @GetMapping("/login")
    public String login(RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Check if the user has the role of Organization or Volunteer
            if (authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ORGANIZATION") ||
                            grantedAuthority.getAuthority().equals("ROLE_VOLUNTEER"))) {
                // Redirect to home if authenticated
                return "redirect:/home";
            }
        }
        return "login"; // Return the login view if not authenticated
    }

    @GetMapping("/signup/volunteer")
    public String registerVolunteer(RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Check if the user has the role of Organization or Volunteer
            if (authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ORGANIZATION") ||
                            grantedAuthority.getAuthority().equals("ROLE_VOLUNTEER"))) {
                // Redirect to home if authenticated
                return "redirect:/home";
            }
        }
        return "volunteer_registration"; // Return the registration view if not authenticated
    }

    @GetMapping("/signup/organization")
    public String registerOrganization(RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Check if the user has the role of Organization or Volunteer
            if (authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ORGANIZATION") ||
                            grantedAuthority.getAuthority().equals("ROLE_VOLUNTEER"))) {
                // Redirect to home if authenticated
                return "redirect:/home";
            }
        }
        return "organization_registration"; // Return the registration view if not authenticated
    }
}