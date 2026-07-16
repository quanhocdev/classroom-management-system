package com.example.classroom.controller.api.admin;


import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/admin")
public class AdminDashboardApiController {



    @GetMapping("/dashboard")
    public Map<String,Object> dashboard(){


        return Map.of(
                "username", "Admin",
                "message", "Admin dashboard loaded"
        );

    }

}