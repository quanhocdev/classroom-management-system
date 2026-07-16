package com.example.classroom.controller.api.student;


import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/student")
public class StudentDashboardApiController {



    @GetMapping("/dashboard")
    public Map<String,Object> dashboard(){


        return Map.of(

            "schedules",
            List.of(
                Map.of(
                    "time","07:30 - 09:00",
                    "subject","Toán",
                    "room","A102",
                    "teacher","Nguyễn Văn A"
                )
            ),


            "assignments",
            List.of(
                Map.of(
                    "title","Bài tập hàm số",
                    "className","10A1",
                    "deadline","Ngày mai"
                )
            )

        );


    }

}