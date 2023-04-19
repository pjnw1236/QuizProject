package com.mysite.sbb.controller;

import com.mysite.sbb.form.AnswerForm;
import com.mysite.sbb.entity.Question;
import com.mysite.sbb.form.QuestionForm;
import com.mysite.sbb.repository.UserRepository;
import com.mysite.sbb.service.QuestionService;
import com.mysite.sbb.entity.SiteUser;
import com.mysite.sbb.service.UserService;
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
    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        Page<Question> paging = questionService.getList(page);
        model.addAttribute("paging", paging);
        return "question_list";
    }

//    @GetMapping(value = "/detail/{id}")
//    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        log.info("auth" + authentication.getName());
//        log.info("isAUth " + authentication.isAuthenticated());
//        if (authentication instanceof AnonymousAuthenticationToken) {
//            Question question = questionService.getQuestion(id);
//            model.addAttribute("question", question);
//        } else {
//            String name = authentication.getName();
//            Question question = questionService.getQuestionByAuthenticated(id, name);
//            model.addAttribute("question", question);
//            log.info("question_viewer", question.getViewer() );
//        }
//        return "question_detail";
//    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;

            String username = auth.getName();
            Boolean bool = false;

            Question question = questionService.getQuestionByUsername(id, username, bool);
            model.addAttribute("question", question);
            return "question_detail";
        } else if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            String email = oauthToken.getPrincipal().getAttribute("email");
            Boolean bool = true;

            Question question = questionService.getQuestionByEmail(id, email, bool);
            model.addAttribute("question", question);
            return "question_oauth_detail";
        } else {
            Question question = questionService.getQuestion(id);
            model.addAttribute("question", question);
            return "question_detail";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/create")
//    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
//        if (bindingResult.hasErrors()) {
//            return "question_form";
//        }
//        SiteUser siteUser = userService.getUser(principal.getName());
//        questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
//        return "redirect:/question/list";
//    }

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

            SiteUser siteUser = userService.getUser(username, bool);
            questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);

            return "redirect:/question/list";
        } else {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            String email = oauthToken.getPrincipal().getAttribute("email");
            Boolean bool = true;

            List<SiteUser> siteUserList = userRepository.findByEmailAndIsOauth(email, bool);
            SiteUser siteUser = siteUserList.get(0);
            questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);

            return "redirect:/question/list";
        }
    }

//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/modify/{id}")
//    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
//        Question question = questionService.getQuestion(id);
//        if (!question.getAuthor().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
//        }
//        questionForm.setSubject(question.getSubject());
//        questionForm.setContent(question.getContent());
//        return "question_form";
//    }

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

//    @PreAuthorize("isAuthenticated()")
//    @PostMapping("/modify/{id}")
//    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal, @PathVariable("id") Integer id) {
//        if (bindingResult.hasErrors()) {
//            return "question_form";
//        }
//        Question question = questionService.getQuestion(id);
//        if (!question.getAuthor().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
//        }
//        questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
//        return String.format("redirect:/question/detail/%s", id);
//    }

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
//
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/delete/{id}")
//    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
//        Question question = questionService.getQuestion(id);
//        if (!question.getAuthor().getUsername().equals(principal.getName())) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
//        }
//        questionService.delete(question);
//        return "redirect:/";
//    }

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

//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/vote/{id}")
//    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
//        Question question = questionService.getQuestion(id);
//        SiteUser siteUser = userService.getUser(principal.getName());
//        questionService.vote(question, siteUser);
//        return String.format("redirect:/question/detail/%s", id);
//    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(@PathVariable("id") Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Question question = questionService.getQuestion(id);

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;

            String username = auth.getName();
            Boolean bool = false;

            SiteUser siteUser = userService.getUser(username, bool);

            questionService.vote(question, siteUser);
            return String.format("redirect:/question/detail/%s", id);
        } else {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

            String email = oauthToken.getPrincipal().getAttribute("email");
            Boolean bool = true;

            List<SiteUser> siteUserList = userRepository.findByEmailAndIsOauth(email, bool);

            questionService.vote(question, siteUserList.get(0));
            return String.format("redirect:/question/detail/%s", id);
        }
    }
}
