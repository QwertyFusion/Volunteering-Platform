package com.example.volunteer_platform.controller.views;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@Slf4j
public class OrganizationViewsController {
    @GetMapping("/o/current_tasks")
    public ModelAndView currentTasks() {
        return new ModelAndView("organization_current_tasks");
    }

    @GetMapping("/o/history")
    public ModelAndView tasksHistory() {
        return new ModelAndView("organization_task_history");
    }

    @GetMapping("/o/profile/edit")
    public ModelAndView profileSettings() {
        return new ModelAndView("organization_profile_settings");
    }

    @GetMapping("/o/profile")
    public ModelAndView profile() {
        return new ModelAndView("organization_profile");
    }
}