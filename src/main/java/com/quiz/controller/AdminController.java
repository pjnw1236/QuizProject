package com.quiz.controller;

import com.quiz.dto.QuizRequestDto;
import com.quiz.dto.QuizResponseDto;
import com.quiz.service.AdminService;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
@AllArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/quiz")
    public String getAdminQuiz(Model model) {
        List<QuizResponseDto> quizResponseDtoList = adminService.getQuizList();
        model.addAttribute("quizResponseDtoList", quizResponseDtoList);
        return "admin/admin_quiz";
    }

    @GetMapping("/board")
    public String getAdminBoard() {
        return "admin/admin_question_list";
    }

    @GetMapping("/quiz/form")
    public String getAdminQuizForm(QuizRequestDto quizRequestDto) {
        log.info(quizRequestDto.toString());
        return "admin/admin_quiz_form";
    }

    @PostMapping("/quiz/form")
    public String createAdminQuiz(@Valid QuizRequestDto quizRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/admin_quiz_form";
        }
        QuizResponseDto quizResponseDto = adminService.createQuiz(quizRequestDto);
        return "redirect:/admin/quiz";
    }
}
