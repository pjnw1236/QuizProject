package com.quiz.controller;

import com.quiz.constant.AuthenticationUtil;
import com.quiz.dto.QuestionRequestDto;
import com.quiz.form.AnswerForm;
import com.quiz.entity.Question;
import com.quiz.repository.MemberRepository;
import com.quiz.service.QuestionService;
import com.quiz.entity.Member;
import com.quiz.service.MemberService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
@Slf4j
public class QuestionController {
    private final QuestionService questionService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "kw", defaultValue = "") String kw, @RequestParam(name = "searchOption", required = false) String searchOption) {
        model.addAttribute("kw", kw);
        model.addAttribute("searchOption", searchOption);
        if (searchOption == null) {
            Page<Question> paging = questionService.getList(page, kw);
            model.addAttribute("paging", paging);
        } else if (searchOption.equals("subjectAndContent")) {
            Page<Question> paging = questionService.getListBySubjectAndContent(page, kw);
            model.addAttribute("paging", paging);
        } else if (searchOption.equals("subject")) {
            Page<Question> paging = questionService.getListBySubject(page, kw);
            model.addAttribute("paging", paging);
        } else if (searchOption.equals("content")) {
            Page<Question> paging = questionService.getListByContent(page, kw);
            model.addAttribute("paging", paging);
        } else if (searchOption.equals("comment")) {
            Page<Question> paging = questionService.getListByAnswerContent(page, kw);
            model.addAttribute("paging", paging);
        } else if (searchOption.equals("username")) {
            Page<Question> paging = questionService.getListByUsername(page, kw);
            model.addAttribute("paging", paging);
        } else {
            Page<Question> paging = questionService.getList(page, kw);
            model.addAttribute("paging", paging);
        }
        return "board/question/list";
    }

    @GetMapping( "/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Member member = AuthenticationUtil.getMember(memberRepository);
        Question question = questionService.getQuestionByMember(member, id);
        model.addAttribute("question", question);
        model.addAttribute("member", member);
        return "board/question/detail";
    }

    @GetMapping("/register")
    public String registerForm(QuestionRequestDto questionRequestDto) {
        return "board/question/form/register";
    }

    @PostMapping
    public String registerQuestion(@Valid QuestionRequestDto questionRequestDto, BindingResult bindingResult) {
        Member member = AuthenticationUtil.getMember(memberRepository);
        if (bindingResult.hasErrors()) {
            return "board/question/form/register";
        }
        questionService.register(questionRequestDto, member);
        return "redirect:/question/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(QuestionRequestDto questionRequestDto, @PathVariable("id") Integer id) {
        Member member = AuthenticationUtil.getMember(memberRepository);
        Question question = questionService.getQuestion(id);
        if (questionService.getPermission(question, member)) {
            questionRequestDto.setSubject(question.getSubject());
            questionRequestDto.setContent(question.getContent());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        return "board/question/form/edit";
    }

    @PatchMapping("/{id}")
    public String editQuestion(@Valid QuestionRequestDto questionRequestDto, BindingResult bindingResult, @PathVariable("id") Integer id, Model model) {
        Member member = AuthenticationUtil.getMember(memberRepository);
        Question question = questionService.getQuestion(id);
        model.addAttribute("id", id);
        if (bindingResult.hasErrors()) {
            return "board/question/form/edit";
        }
        if (questionService.getPermission(question, member)) {
            questionService.edit(question, questionRequestDto);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        }
        return String.format("redirect:/question/%s", id);
    }

    @DeleteMapping("/{id}")
    public String questionDelete(@PathVariable("id") Integer id) {
        Member member = AuthenticationUtil.getMember(memberRepository);
        Question question = questionService.getQuestion(id);
        if (questionService.getPermission(question, member)) {
            questionService.delete(question);
            return "redirect:/question/list";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        }
    }

    @PostMapping("/vote/{id}")
    public String questionVote(@PathVariable("id") Integer id) {
        Member member = AuthenticationUtil.getMember(memberRepository);
        Question question = questionService.getQuestion(id);
        questionService.vote(question, member);
        return String.format("redirect:/question/%s", id);
    }
}
