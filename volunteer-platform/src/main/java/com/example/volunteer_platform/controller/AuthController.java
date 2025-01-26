package com.example.volunteer_platform.controller;

import com.example.volunteer_platform.dto.VolunteerDto;
import com.example.volunteer_platform.dto.OrganizationDto;
import com.example.volunteer_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * Controller for authentication and registration.
 */
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * Display the volunteer registration form.
     *
     * @param model Model object to pre-populate the form.
     * @return The name of the volunteer registration view.
     */
    @GetMapping("/register/volunteer")
    public String volunteerRegister(Model model) {
        model.addAttribute("volunteerDto", new VolunteerDto()); // Add VolunteerDto to model
        return "volunteer_registration"; // Ensure volunteer_registration.html exists.
    }

    /**
     * Handle the registration of a volunteer.
     *
     * @param volunteerDto The form data from the volunteer registration form.
     * @param bindingResult Binding result for validating the form.
     * @return Redirects to the login page on successful registration.
     */
    @PostMapping("/register/volunteer")
    public String registerVolunteer(@Valid @ModelAttribute("volunteerDto") VolunteerDto volunteerDto,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "volunteer_registration"; // Show validation errors if they exist
        }
        userService.saveVolunteer(volunteerDto); // Save the volunteer using service
        return "redirect:/login"; // Redirect to login after successful registration
    }

    /**
     * Display the organization registration form.
     *
     * @param model Model object to pre-populate the form.
     * @return The name of the organization registration view.
     */
    @GetMapping("/register/organization")
    public String organizationRegister(Model model) {
        model.addAttribute("organizationDto", new OrganizationDto()); // Add OrganizationDto to model
        return "organization_registration"; // Ensure organization_registration.html exists.
    }

    /**
     * Handle the registration of an organization.
     *
     * @param organizationDto The form data from the organization registration form.
     * @param bindingResult Binding result for validating the form.
     * @return Redirects to the login page on successful registration.
     */
    @PostMapping("/register/organization")
    public String registerOrganization(@Valid @ModelAttribute("organizationDto") OrganizationDto organizationDto,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "organization_registration"; // Show validation errors if they exist
        }
        userService.saveOrganization(organizationDto); // Save the organization using service
        return "redirect:/login"; // Redirect to login after successful registration
    }
}
