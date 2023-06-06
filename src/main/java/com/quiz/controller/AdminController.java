package com.quiz.controller;

import com.quiz.constant.AuthenticationUtil;
import com.quiz.dto.AnswerRequestDto;
import com.quiz.entity.Member;
import com.quiz.entity.Question;
import com.quiz.repository.MemberRepository;
import com.quiz.service.AdminService;
import com.quiz.service.QuestionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final QuestionService questionService;
    private final MemberRepository memberRepository;
    private final AdminService adminService;

    @GetMapping("/question/list")
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
        return "admin/question/list";
    }

    @GetMapping( "/question/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerRequestDto answerRequestDto) {
        Member member = AuthenticationUtil.getMember(memberRepository);
        Question question = questionService.getQuestionByMember(member, id);
        model.addAttribute("question", question);
        model.addAttribute("member", member);
        return "admin/question/detail";
    }

    @DeleteMapping( "/question/{id}")
    public String deleteQuestion(@PathVariable("id") Integer id) {
        adminService.deleteQuestion(id);
//        return "admin/question/list";
        return "redirect:/admin/question/list";
    }

    @DeleteMapping("/question/answer/{id}")
    public String deleteAnswer(@PathVariable("id") Integer id, Model model) {
        Question question = adminService.getAnswer(id).getQuestion();
        adminService.deleteAnswer(id);
//        return "admin/question/detail";
        return String.format("redirect:/admin/question/%s", question.getId());
    }
}
