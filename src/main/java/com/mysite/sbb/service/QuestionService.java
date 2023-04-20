package com.mysite.sbb.service;

import com.mysite.sbb.exception.DataNotFoundException;
import com.mysite.sbb.entity.Question;
import com.mysite.sbb.repository.QuestionRepository;
import com.mysite.sbb.entity.SiteUser;
import com.mysite.sbb.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return questionRepository.findAll(pageable);
    }

    // 제목+내용, 제목, 내용, 댓글, 닉네임
    public Page<Question> getListBySubjectAndContent(int page, String kw) {
        List<Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return questionRepository.findAllBySubjectAndContent(kw, pageable);
    }
    public Page<Question> getListBySubject(int page, String kw) {
        List<Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return questionRepository.findAllBySubject(kw, pageable);
    }
    public Page<Question> getListByContent(int page, String kw) {
        List<Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return questionRepository.findAllByContent(kw, pageable);
    }
    public Page<Question> getListByAnswerContent(int page, String kw) {
        List<Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return questionRepository.findAllByAnswerContent(kw, pageable);
    }
    public Page<Question> getListByUsername(int page, String kw) {
        List<Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return questionRepository.findAllByAuthor(kw, pageable);
    }

    public Question getQuestion(Integer id) {
        Optional<Question> question = questionRepository.findById(id);
        if (question.isPresent()) {
            return question.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public Question getQuestionByUsername(Integer id, String username, Boolean bool) {
        SiteUser user = userService.getUser(username, bool);
        Question question = this.getQuestion(id);
        question.getViewer().add(user);
        questionRepository.save(question);
        return question;
    }

    public Question getQuestionByEmail(Integer id, String email, Boolean bool) {
        List<SiteUser> siteUserList = userRepository.findByEmailAndIsOauth(email, bool);
        Question question = this.getQuestion(id);
        question.getViewer().add(siteUserList.get(0));
        questionRepository.save(question);
        return question;
    }

    public void create(String subject, String content, SiteUser user) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        questionRepository.save(q);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        questionRepository.save(question);
    }

    public void delete(Question question) {
        questionRepository.delete(question);
    }

    public void vote(Question question, SiteUser siteUser) {
        question.getVoter().add(siteUser);
        questionRepository.save(question);
    }
}
