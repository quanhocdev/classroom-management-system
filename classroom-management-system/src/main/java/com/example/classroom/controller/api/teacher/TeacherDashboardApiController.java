package com.example.classroom.controller.api.teacher;


import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/teacher")
public class TeacherDashboardApiController {



    @GetMapping("/dashboard")
    public Map<String,Object> dashboard(){


        return Map.of(
                "totalClasses", 5,
                "totalStudents", 100
        );

    }

}