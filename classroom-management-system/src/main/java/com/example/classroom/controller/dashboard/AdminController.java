package com.example.classroom.controller.dashboard;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/trang-chu")
    public String showTrangChu() {
        return "admin/trang-chu-admin";
    }
    
}
