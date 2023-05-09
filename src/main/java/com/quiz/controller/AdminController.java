package com.quiz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminController {


    @GetMapping("/quiz")
    public String getAdminQuiz() {
        return "admin/admin_quiz";
    }

    @GetMapping("/board")
    public String getAdminBoard() {
        return "admin/admin_question_list";
    }
}
