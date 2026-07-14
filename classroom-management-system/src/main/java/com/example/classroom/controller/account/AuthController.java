package com.example.classroom.controller.account;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.classroom.dto.account.RegisterRequestDTO;
import com.example.classroom.service.account.AuthService;



@Controller
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private AuthService authService;



    /*
     * Trang đăng nhập
     *
     * Spring Security sẽ xử lý POST /login
     */
    @GetMapping("/login")
    public String loginPage() {

        return "account/login";

    }



    /*
     * Đăng ký tài khoản
     */
    @PostMapping("/register")
    public String register(
            @RequestBody RegisterRequestDTO request,
            Model model
    ) {


        authService.register(request);



        model.addAttribute(
                "message",
                "Đăng ký thành công"
        );



        return "account/login";

    }

}