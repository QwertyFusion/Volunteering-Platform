package com.example.volunteer_platform.controller.views;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class VolunteerViewsController {
    @GetMapping("/v/opportunities")
    public ModelAndView viewOpportunities() {
        return new ModelAndView("volunteer_opportunities");
    }
}