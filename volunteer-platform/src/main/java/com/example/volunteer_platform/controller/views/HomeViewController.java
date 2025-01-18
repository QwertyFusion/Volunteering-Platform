package com.example.volunteer_platform.controller.views;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeViewController {
    @GetMapping({"/","/index.html"})
    public ModelAndView home(){
        return new ModelAndView("index");
    }
}
