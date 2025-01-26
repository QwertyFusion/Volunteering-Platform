package com.example.volunteer_platform.controller.views;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class RatingViewsController {
    @GetMapping("/rankings/organizations")
    public ModelAndView topOrganizations() {
        return new ModelAndView("top_organizations");
    }

    @GetMapping("/rankings/volunteers")
    public ModelAndView topVolunteers() {
        return new ModelAndView("top_volunteers");
    }
}
