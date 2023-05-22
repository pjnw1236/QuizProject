package com.quiz.controller;

import com.quiz.dto.MemberQuizDto;
import com.quiz.dto.QuizRequestDto;
import com.quiz.dto.QuizResponseDto;
import com.quiz.entity.Member;
import com.quiz.repository.UserRepository;
import com.quiz.service.QuizService;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@AllArgsConstructor
@Slf4j
public class QuizController {
    private final QuizService quizService;
    private final UserRepository userRepository;

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
    public String getQuizDetail(@PathVariable("id") Long id, Model model) {
        QuizResponseDto quizResponseDto = quizService.getQuizResponseDtoByQuizNumber(id);
        model.addAttribute("quizResponseDto", quizResponseDto);
        return "admin/quiz_detail";
    }

    @GetMapping("/admin/quiz/editform/{id}")
    public String getQuizEditForm(@PathVariable("id") Long id, Model model) {
        QuizRequestDto quizRequestDto = quizService.getQuizRequestDto(id);
        model.addAttribute("quizRequestDto", quizRequestDto);
        return "admin/quiz_edit_form";
    }

    @PatchMapping("/admin/quiz/editform/{id}")
    public String patchQuizDetail(@PathVariable("id") Long id, Model model, @Valid QuizRequestDto quizRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/quiz_edit_form";
        }
        quizService.patchQuiz(id, quizRequestDto);
        return String.format("redirect:/admin/quiz/%s", id);
    }

    @GetMapping("/quiz/python")
    public String getQuizPython() {
        return "quiz/python_home";
    }

    @GetMapping("/quiz/python/{id}")
    public String getPythonQuizDetail(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
        QuizResponseDto quizResponseDto = quizService.getQuizResponseDtoByQuizNumber(id);
        model.addAttribute("quizResponseDto", quizResponseDto);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
            List<Member> memberList = userRepository.findByUsernameAndIsOauth(auth.getName(), false);
            if (memberList.size() > 0) {
                Member member = memberList.get(0);
                log.info("===== member =====");
                log.info(member.getUsername());
                log.info(member.getEmail());
                log.info(member.getIsOauth().toString());
                log.info("===== member =====");
            }
        } else {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            List<Member> memberList = userRepository.findByEmailAndIsOauth(oauthToken.getPrincipal().getAttribute("email"), true);
            if (memberList.size() > 0) {
                Member member = memberList.get(0);
                log.info("===== member =====");
                log.info(member.getUsername());
                log.info(member.getEmail());
                log.info(member.getIsOauth().toString());
                log.info("===== member =====");
            }
        }

        String check = request.getParameter("endTime");
        if (check != null) {
            log.info("== endTime == " + check);
        } else {
            log.info("== endTime NULL ==");
        }

        return "quiz/python_quiz";
    }

    @GetMapping("/quiz/java")
    public String getQuizJava() {
        return "quiz/java_home";
    }

    @GetMapping("/quiz/java/{id}")
    public String getJavaQuizDetail(@PathVariable("id") Long id, Model model) {
        QuizResponseDto quizResponseDto = quizService.getQuizResponseDtoByQuizNumber(id);
        model.addAttribute("quizResponseDto", quizResponseDto);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
            List<Member> memberList = userRepository.findByUsernameAndIsOauth(auth.getName(), false);
            if (memberList.size() > 0) {
                Member member = memberList.get(0);
                log.info("===== member =====");
                log.info(member.getUsername());
                log.info(member.getEmail());
                log.info(member.getIsOauth().toString());
                log.info("===== member =====");
            }
        } else {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            List<Member> memberList = userRepository.findByEmailAndIsOauth(oauthToken.getPrincipal().getAttribute("email"), true);
            if (memberList.size() > 0) {
                Member member = memberList.get(0);
                log.info("===== member =====");
                log.info(member.getUsername());
                log.info(member.getEmail());
                log.info(member.getIsOauth().toString());
                log.info("===== member =====");
            }
        }

        return "quiz/java_quiz";
    }

    @PostMapping("/quiz/python/{id}")
    @ResponseBody
    public MemberQuizDto postPythonQuizAnswer(@PathVariable("id") Long id, @RequestBody MemberQuizDto memberQuizDto, Model model) {
        QuizResponseDto quizResponseDto = quizService.getQuizResponseDtoByQuizNumber(id);
        log.info("=============================================");
        log.info(memberQuizDto.getAnswerNumber() + "번을 고르셨습니다.");
        log.info("=============================================");
        return memberQuizDto;
    }
}
