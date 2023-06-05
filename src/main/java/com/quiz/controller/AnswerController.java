package com.quiz.controller;

import com.quiz.constant.AuthenticationUtil;
import com.quiz.dto.AnswerRequestDto;
import com.quiz.entity.Answer;
import com.quiz.repository.MemberRepository;
import com.quiz.service.AnswerService;
import com.quiz.form.AnswerForm;
import com.quiz.entity.Question;
import com.quiz.service.QuestionService;
import com.quiz.entity.Member;
import com.quiz.service.MemberService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class AnswerController {
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @PostMapping("/{id}/answer")
    public String createAnswer(Model model, @PathVariable("id") Integer id, @Valid AnswerRequestDto answerRequestDto, BindingResult bindingResult) {
        Member member = AuthenticationUtil.getMember(memberRepository);
        Question question = questionService.getQuestion(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("question", question);
            return "board/question/detail";
        }

        answerService.create(question, answerRequestDto, member);
        return String.format("redirect:/question/%s", id);
    }

    @GetMapping("/answer/edit/{id}")
    public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Answer answer = answerService.getAnswer(id);

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;

            String username = auth.getName();
            Boolean bool = false;

            if (answer.getAuthor().getUsername().equals(username) && answer.getAuthor().getIsOauth().equals(bool)) {
                answerForm.setContent(answer.getContent());
                return "answer_form";
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
            }
        } else {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            String email = oauthToken.getPrincipal().getAttribute("email");
            Boolean bool = true;

            List<Member> memberList = memberRepository.findByEmailAndIsOauth(email, bool);

            if (answer.getAuthor().getEmail().equals(email) && answer.getAuthor().getIsOauth().equals(bool)) {
                answerForm.setContent(answer.getContent());
                return "answer_form";
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
            }
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult, @PathVariable("id") Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Answer answer = answerService.getAnswer(id);

        if (bindingResult.hasErrors()) {
            return "answer_form";
        }

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;

            String username = auth.getName();
            Boolean bool = false;

            if (answer.getAuthor().getUsername().equals(username) && answer.getAuthor().getIsOauth().equals(bool)) {
                answerService.modify(answer, answerForm.getContent());
                return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
            }
        } else {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            String email = oauthToken.getPrincipal().getAttribute("email");
            Boolean bool = true;

            List<Member> memberList = memberRepository.findByEmailAndIsOauth(email, bool);

            if (answer.getAuthor().getEmail().equals(email) && answer.getAuthor().getIsOauth().equals(bool)) {
                answerService.modify(answer, answerForm.getContent());
                return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
            }
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String answerDelete(@PathVariable("id") Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Answer answer = answerService.getAnswer(id);

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;

            String username = auth.getName();
            Boolean bool = false;

            if (answer.getAuthor().getUsername().equals(username) && answer.getAuthor().getIsOauth().equals(bool)) {
                answerService.delete(answer);
                return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
            }
        } else {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            String email = oauthToken.getPrincipal().getAttribute("email");
            Boolean bool = true;

            List<Member> memberList = memberRepository.findByEmailAndIsOauth(email, bool);

            if (answer.getAuthor().getEmail().equals(email) && answer.getAuthor().getIsOauth().equals(bool)) {
                answerService.delete(answer);
                return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
            }
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String answerVote(@PathVariable("id") Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Answer answer = answerService.getAnswer(id);

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;

            String username = auth.getName();
            Boolean bool = false;

            Member member = memberService.getUser(username, bool);

            answerService.vote(answer, member);
            return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
        } else {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            String email = oauthToken.getPrincipal().getAttribute("email");
            Boolean bool = true;

            List<Member> memberList = memberRepository.findByEmailAndIsOauth(email, bool);

            answerService.vote(answer, memberList.get(0));
            return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
        }
    }
}
