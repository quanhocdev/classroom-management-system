package com.example.classroom.controller.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/auth")
public class DashboardAuthController {
    
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }
    @GetMapping("/register")
    public String showRegister() {
        return "register";
    }

}
