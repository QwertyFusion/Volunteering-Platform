package com.example.volunteer_platform.controller.views;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class ViewsController {
    @GetMapping({"/index.html", "/home"})
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    /**
     * Display the login page.
     *
     * @return The name of the login view.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup/volunteer")
    public String registerVolunteer() {
        return "volunteer_registration";
    }

    @GetMapping("/signup/organization")
    public String registerOrganization() {
        return "organisation_registration";
    }
}