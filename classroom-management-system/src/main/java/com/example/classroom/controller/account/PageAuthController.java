package com.example.classroom.controller.account;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/auth")
public class PageAuthController {



    @GetMapping("/login")
    public String loginPage() {

        return "account/login";

    }


    @GetMapping("/register")
    public String registerPage() {

        return "account/register";

    }

}