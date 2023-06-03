package com.quiz.controller;

import com.quiz.constant.AuthenticationUtil;
import com.quiz.form.AnswerForm;
import com.quiz.entity.Question;
import com.quiz.form.QuestionForm;
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
import org.springframework.web.bind.annotation.GetMapping;
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;

            String username = auth.getName();
            Boolean bool = false;

            Member member = memberService.getUser(username, bool);
            questionService.create(questionForm.getSubject(), questionForm.getContent(), member);

            return "redirect:/question/list";
        } else {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            String email = oauthToken.getPrincipal().getAttribute("email");
            Boolean bool = true;

            List<Member> memberList = memberRepository.findByEmailAndIsOauth(email, bool);
            Member member = memberList.get(0);
            questionService.create(questionForm.getSubject(), questionForm.getContent(), member);

            return "redirect:/question/list";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Question question = questionService.getQuestion(id);

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;

            String username = auth.getName();
            Boolean bool = false;

            if (question.getAuthor().getUsername().equals(username) && question.getAuthor().getIsOauth().equals(bool)) {
                questionForm.setSubject(question.getSubject());
                questionForm.setContent(question.getContent());
                return "question_form";
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
            }
        } else {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            String email = oauthToken.getPrincipal().getAttribute("email");
            Boolean bool = true;

            if (question.getAuthor().getEmail().equals(email) && question.getAuthor().getIsOauth().equals(bool)) {
                questionForm.setSubject(question.getSubject());
                questionForm.setContent(question.getContent());
                return "question_form";
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
            }
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, @PathVariable("id") Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Question question = questionService.getQuestion(id);

        if (bindingResult.hasErrors()) {
            return "question_form";
        }

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;

            String username = auth.getName();
            Boolean bool = false;

            if (question.getAuthor().getUsername().equals(username) && question.getAuthor().getIsOauth().equals(bool)) {
                questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
                return String.format("redirect:/question/detail/%s", id);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
            }
        } else {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            String email = oauthToken.getPrincipal().getAttribute("email");
            Boolean bool = true;

            if (question.getAuthor().getEmail().equals(email) && question.getAuthor().getIsOauth().equals(bool)) {
                questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
                return String.format("redirect:/question/detail/%s", id);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
            }
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(@PathVariable("id") Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Question question = questionService.getQuestion(id);

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;

            String username = auth.getName();
            Boolean bool = false;

            if (question.getAuthor().getUsername().equals(username) && question.getAuthor().getIsOauth().equals(bool)) {
                questionService.delete(question);
                return "redirect:/";
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
            }
        } else {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            String email = oauthToken.getPrincipal().getAttribute("email");
            Boolean bool = true;

            if (question.getAuthor().getEmail().equals(email) && question.getAuthor().getIsOauth().equals(bool)) {
                questionService.delete(question);
                return "redirect:/";
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
            }
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(@PathVariable("id") Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Question question = questionService.getQuestion(id);

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;

            String username = auth.getName();
            Boolean bool = false;

            Member member = memberService.getUser(username, bool);

            questionService.vote(question, member);
            return String.format("redirect:/question/detail/%s", id);
        } else {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            String email = oauthToken.getPrincipal().getAttribute("email");
            Boolean bool = true;

            List<Member> memberList = memberRepository.findByEmailAndIsOauth(email, bool);

            questionService.vote(question, memberList.get(0));
            return String.format("redirect:/question/detail/%s", id);
        }
    }
}
