package com.example.classroom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TrangChuPublicController {

    @GetMapping("/trang-chu")
    public String showTrangChu() {
        return "trang-chu-public";
    }
}