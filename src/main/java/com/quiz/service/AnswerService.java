package com.quiz.service;

import com.quiz.dto.AnswerRequestDto;
import com.quiz.exception.DataNotFoundException;
import com.quiz.entity.Answer;
import com.quiz.repository.AnswerRepository;
import com.quiz.entity.Question;
import com.quiz.entity.Member;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AnswerService {
    private final AnswerRepository answerRepository;

    public void create(Question question, AnswerRequestDto answerRequestDto, Member member) {
        if (member != null) {
            Answer answer = new Answer();
            answer.setContent(answerRequestDto.getContent());
            answer.setCreateDate(LocalDateTime.now());
            answer.setQuestion(question);
            answer.setAuthor(member);
            answerRepository.save(answer);
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = answerRepository.findById(id);
        if (answer.isPresent()) {
            return answer.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public boolean getPermission(Answer answer, Member member) {
        if (answer.getAuthor() == member) {
            return true;
        }
        return false;
    }

    public void modify(Answer answer, AnswerRequestDto answerRequestDto) {
        answer.setContent(answerRequestDto.getContent());
        answer.setModifyDate(LocalDateTime.now());
        answerRepository.save(answer);
    }

    public void delete(Answer answer) {
        answerRepository.delete(answer);
    }

    public void vote(Answer answer, Member member) {
        if (member != null) {
            answer.getVoter().add(member);
            answerRepository.save(answer);
        } else {
            throw new DataNotFoundException("member not found");
        }
    }
}
