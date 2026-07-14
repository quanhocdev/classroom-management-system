package com.example.classroom.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TrangChuPublicController {

    @GetMapping("/trang-chu")
    public String showTrangChu() {
        return "trang-chu-public";
    }
}