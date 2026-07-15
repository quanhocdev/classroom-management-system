package com.example.classroom.controller.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/teacher")
public class TeacherController {
    
    @GetMapping("/trang-chu")
    public String showTrangChu() {
        return "teacher/trang-chu-teacher";
    }


    @GetMapping("/classes")
    public String showClasses() {
        return "teacher/classes-teacher";
    }

    @GetMapping("/grades")
    public String showGrades() {
        return "teacher/grades-teacher";
    }


}
