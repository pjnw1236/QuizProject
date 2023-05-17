package com.quiz.service;

import com.quiz.dto.QuizRequestDto;
import com.quiz.dto.QuizResponseDto;
import com.quiz.entity.Quiz;
import com.quiz.exception.DataNotFoundException;
import com.quiz.repository.QuizRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class QuizService {
    private final QuizRepository quizRepository;

    public QuizResponseDto createQuiz(QuizRequestDto quizRequestDto) {
        Quiz quiz = Quiz.dtoToEntity(quizRequestDto);
        quizRepository.save(quiz);
        QuizResponseDto quizResponseDto = Quiz.entityToDto(quiz);
        return quizResponseDto;
    }

    public List<QuizResponseDto> getQuizList() {
        List<Quiz> quizList = quizRepository.findAll();
        List<QuizResponseDto> quizResponseDtoList = Quiz.entityListToDtoList(quizList);
        return quizResponseDtoList;
    }

    public QuizResponseDto getQuiz(Long id) {
        Quiz quiz = quizRepository.findById(id);
        if (quiz == null) {
            throw new DataNotFoundException("해당 퀴즈는 존재하지 않습니다.");
        }
        QuizResponseDto quizResponseDto = Quiz.entityToDto(quiz);
        return quizResponseDto;
    }

    public QuizRequestDto getQuizRequestDto(Long id) {
        Quiz quiz = quizRepository.findById(id);
        if (quiz == null) {
            throw new DataNotFoundException("해당 퀴즈는 존재하지 않습니다.");
        }
        QuizRequestDto quizRequestDto = Quiz.entityToRequestDto(quiz);
        return quizRequestDto;
    }

    public void patchQuiz(Long id, QuizRequestDto quizRequestDto) {
        Quiz quiz = quizRepository.findById(id);
        if (quiz == null) {
            throw new DataNotFoundException("해당 퀴즈는 존재하지 않습니다.");
        }
        quiz.setTitle(quizRequestDto.getTitle());
        quiz.setPythonContent(quizRequestDto.getPythonContent());
        quiz.setJavaContent(quizRequestDto.getJavaContent());
        quiz.setFirst(quizRequestDto.getFirst());
        quiz.setSecond(quizRequestDto.getSecond());
        quiz.setThird(quizRequestDto.getThird());
        quiz.setFourth(quizRequestDto.getFourth());
        quiz.setQuizAnswer(quizRequestDto.getQuizAnswer());
    }
}
