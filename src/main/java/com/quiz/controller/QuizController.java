package com.quiz.controller;

import com.quiz.constant.AuthenticationUtil;
import com.quiz.constant.QuizType;
import com.quiz.dto.MemberQuizDto;
import com.quiz.dto.MemberQuizResultDto;
import com.quiz.dto.QuizRequestDto;
import com.quiz.dto.QuizResponseDto;
import com.quiz.entity.Member;
import com.quiz.entity.MemberQuiz;
import com.quiz.repository.UserRepository;
import com.quiz.service.QuizService;
import java.util.List;
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
    public String patchQuizDetail(@PathVariable("id") Long id, @Valid QuizRequestDto quizRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/quiz_edit_form";
        }
        quizService.patchQuiz(id, quizRequestDto);
        return String.format("redirect:/admin/quiz/%s", id);
    }

    @GetMapping("/quiz/{quizType:python|java}")
    public String getQuizHome(@PathVariable("quizType") String quizType, Model model) {
        model.addAttribute("quizType", quizType);
        return "quiz/user/home";
    }

    @GetMapping("/quiz/{quizType}/result")
    public String getQuizResult(@PathVariable("quizType") String quizType, Model model) {
        Member member = AuthenticationUtil.getMember(userRepository);
        List<MemberQuizResultDto> memberQuizResultDtoList = null;

        double score = 0;
        memberQuizResultDtoList = quizService.getQuizResult(member, quizType);

        for (MemberQuizResultDto memberQuizResultDto : memberQuizResultDtoList) {
            if (memberQuizResultDto.isRight()) {
                score += 1;
            }
        }
        score /= memberQuizResultDtoList.size();

        model.addAttribute("quizType", quizType);
        model.addAttribute("memberQuizResultDtoList", memberQuizResultDtoList);
        model.addAttribute("score", score*100);
        return "quiz/user/result";
    }

    @GetMapping("/quiz/{quizType}/{id}")
    public String getQuizDetail(@PathVariable("quizType") String quizType, @PathVariable("id") Long id, Model model) {
        Member member = AuthenticationUtil.getMember(userRepository);
        QuizResponseDto quizResponseDto = quizService.getQuizResponseDtoByQuizNumber(id);
        model.addAttribute("quizType", quizType);
        model.addAttribute("quizResponseDto", quizResponseDto);
        MemberQuiz memberQuiz = null;
        if (quizType.equals("python")) {
            memberQuiz = member.getMemberQuizByQuizNumber(id, QuizType.Python);
        } else if (quizType.equals("java")) {
            memberQuiz = member.getMemberQuizByQuizNumber(id, QuizType.Java);
        }
        if (memberQuiz != null) {
            model.addAttribute("memberQuizAnswer", memberQuiz.getMemberQuizAnswer());
        }
        return "quiz/user/quiz";
    }

    @PostMapping("/quiz/python")
    @ResponseBody
    public MemberQuizDto postPythonQuizAnswer(@RequestBody MemberQuizDto memberQuizDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
            List<Member> memberList = userRepository.findByUsernameAndIsOauth(auth.getName(), false);
            if (memberList.size() > 0) {
                Member member = memberList.get(0);
                quizService.solvingPythonQuiz(member, memberQuizDto);
            }
        } else {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            List<Member> memberList = userRepository.findByEmailAndIsOauth(oauthToken.getPrincipal().getAttribute("email"), true);
            if (memberList.size() > 0) {
                Member member = memberList.get(0);
                quizService.solvingPythonQuiz(member, memberQuizDto);
            }
        }
        return memberQuizDto;
    }

    @PostMapping("/quiz/java")
    @ResponseBody
    public MemberQuizDto postJavaQuizAnswer(@RequestBody MemberQuizDto memberQuizDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
            List<Member> memberList = userRepository.findByUsernameAndIsOauth(auth.getName(), false);
            if (memberList.size() > 0) {
                Member member = memberList.get(0);
                quizService.solvingJavaQuiz(member, memberQuizDto);
            }
        } else {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            List<Member> memberList = userRepository.findByEmailAndIsOauth(oauthToken.getPrincipal().getAttribute("email"), true);
            if (memberList.size() > 0) {
                Member member = memberList.get(0);
                quizService.solvingJavaQuiz(member, memberQuizDto);
            }
        }
        return memberQuizDto;
    }
}
