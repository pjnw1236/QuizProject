package com.quiz.service;

import com.quiz.entity.Answer;
import com.quiz.exception.DataNotFoundException;
import com.quiz.repository.AnswerRepository;
import com.quiz.repository.QuestionRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public void deleteQuestion(Integer id) {
        questionRepository.deleteById(id);
    }

    public void deleteAnswer(Integer id) {
        answerRepository.deleteById(id);
    }

    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = answerRepository.findById(id);
        if (answer.isEmpty()) {
            throw new DataNotFoundException("answer not found");
        }
        return answer.get();
    }
}
