package com.quiz.service;

import com.quiz.dto.QuestionRequestDto;
import com.quiz.exception.DataNotFoundException;
import com.quiz.entity.Question;
import com.quiz.form.QuestionForm;
import com.quiz.repository.QuestionRepository;
import com.quiz.entity.Member;
import com.quiz.repository.MemberRepository;
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
    private final MemberService memberService;
    private final MemberRepository memberRepository;

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

    // @GetMapping( "/{id}") 시작
    public Question getQuestionByMember(Member member, Integer id) {
        Question question = this.getQuestion(id);
        if (member != null) {
            question.getViewer().add(member);
            questionRepository.save(question);
        }
        return question;
    }
    // @GetMapping( "/{id}") 끝

    public Question getQuestionByEmail(Integer id, String email, Boolean bool) {
        List<Member> memberList = memberRepository.findByEmailAndIsOauth(email, bool);
        Question question = this.getQuestion(id);
        question.getViewer().add(memberList.get(0));
        questionRepository.save(question);
        return question;
    }

    public void register(QuestionRequestDto questionRequestDto, Member member) {
        Question q = new Question();
        q.setSubject(questionRequestDto.getSubject());
        q.setContent(questionRequestDto.getContent());
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(member);
        questionRepository.save(q);
    }

    public boolean getPermission(Question question, Member member) {
        if (question.getAuthor() == member) {
            return true;
        }
        return false;
    }

    public void edit(Question question, QuestionRequestDto questionRequestDto) {
        question.setSubject(questionRequestDto.getSubject());
        question.setContent(questionRequestDto.getContent());
        question.setModifyDate(LocalDateTime.now());
        this.questionRepository.save(question);
    }

    public void delete(Question question) {
        questionRepository.delete(question);
    }

    public void vote(Question question, Member member) {
        if (member == null) {
            throw new DataNotFoundException("member not found");
        }
        question.getVoter().add(member);
        questionRepository.save(question);
    }
}
