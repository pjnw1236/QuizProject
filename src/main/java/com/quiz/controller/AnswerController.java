package com.quiz.controller;

import com.quiz.constant.AuthenticationUtil;
import com.quiz.dto.AnswerRequestDto;
import com.quiz.entity.Answer;
import com.quiz.repository.MemberRepository;
import com.quiz.service.AnswerService;
import com.quiz.entity.Question;
import com.quiz.service.QuestionService;
import com.quiz.entity.Member;
import com.quiz.service.MemberService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    public String answerModify(AnswerRequestDto answerRequestDto, @PathVariable("id") Integer id) {
        Member member = AuthenticationUtil.getMember(memberRepository);
        Answer answer = answerService.getAnswer(id);
        if (answerService.getPermission(answer, member)) {
            answerRequestDto.setContent(answer.getContent());
            return "board/answer/form/edit";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
    }

    @PatchMapping("/answer/{id}")
    public String answerModify(@Valid AnswerRequestDto answerRequestDto, BindingResult bindingResult, @PathVariable("id") Integer id) {
        Member member = AuthenticationUtil.getMember(memberRepository);
        Answer answer = answerService.getAnswer(id);
        if (bindingResult.hasErrors()) {
            return "board/answer/form/edit";
        }
        if (answerService.getPermission(answer, member)) {
            answerService.modify(answer, answerRequestDto);
            return String.format("redirect:/question/%s", answer.getQuestion().getId());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
    }

    @DeleteMapping("/answer/{id}")
    public String answerDelete(@PathVariable("id") Integer id) {
        Member member = AuthenticationUtil.getMember(memberRepository);
        Answer answer = answerService.getAnswer(id);
        if (answerService.getPermission(answer, member)) {
            answerService.delete(answer);
            return String.format("redirect:/question/%s", answer.getQuestion().getId());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
    }

    @PostMapping("/answer/vote/{id}")
    public String answerVote(@PathVariable("id") Integer id) {
        Member member = AuthenticationUtil.getMember(memberRepository);
        Answer answer = answerService.getAnswer(id);
        answerService.vote(answer, member);
        return String.format("redirect:/question/%s", answer.getQuestion().getId());
    }
}
