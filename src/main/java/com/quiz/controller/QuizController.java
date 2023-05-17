package com.quiz.controller;

import com.quiz.dto.QuizRequestDto;
import com.quiz.dto.QuizResponseDto;
import com.quiz.service.QuizService;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
@Slf4j
public class QuizController {
    private final QuizService quizService;

    @GetMapping("/admin/quiz")
    public String getAdminQuiz(Model model) {
        List<QuizResponseDto> quizResponseDtoList = quizService.getQuizList();
        model.addAttribute("quizResponseDtoList", quizResponseDtoList);
        return "admin/admin_quiz";
    }

    @GetMapping("/admin/board")
    public String getAdminBoard() {
        return "admin/admin_question_list";
    }

    @GetMapping("/admin/quiz/form")
    public String getAdminQuizForm(QuizRequestDto quizRequestDto) {
        log.info(quizRequestDto.toString());
        return "admin/quiz_form";
    }

    @PostMapping("/admin/quiz/form")
    public String createAdminQuiz(@Valid QuizRequestDto quizRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/quiz_form";
        }
        QuizResponseDto quizResponseDto = quizService.createQuiz(quizRequestDto);
        return "redirect:/admin/quiz";
    }

    @GetMapping("/admin/quiz/{id}")
    public String getQuizDetail(@PathVariable("id") Integer id, Model model) {
        QuizResponseDto quizResponseDto = quizService.getQuiz(Long.valueOf(id));
        model.addAttribute("quizResponseDto", quizResponseDto);
        return "admin/quiz_detail";
    }

    @GetMapping("/admin/quiz/editform/{id}")
    public String getQuizEditForm(@PathVariable("id") Integer id, Model model) {
        QuizRequestDto quizRequestDto = quizService.getQuizRequestDto(Long.valueOf(id));
        model.addAttribute("quizRequestDto", quizRequestDto);
        return "admin/quiz_edit_form";
    }

    @PatchMapping("/admin/quiz/editform/{id}")
    public String patchQuizDetail(@PathVariable("id") Integer id, Model model, @Valid QuizRequestDto quizRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/quiz_edit_form";
        }
        quizService.patchQuiz(Long.valueOf(id), quizRequestDto);
        return String.format("redirect:/admin/quiz/%s", id);
    }

    @GetMapping("/quiz/python")
    public String getQuizPython() {
        return "quiz/python_home";
    }

    @GetMapping("/quiz/java")
    public String getQuizJava() {
        return "quiz/java_home";
    }
}
