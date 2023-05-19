package com.quiz.service;

import com.quiz.dto.QuizRequestDto;
import com.quiz.dto.QuizResponseDto;
import com.quiz.entity.Quiz;
import com.quiz.exception.DataNotFoundException;
import com.quiz.repository.QuizRepository;
import java.util.List;
import java.util.Optional;
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

    public QuizResponseDto getQuizResponseDtoById(Long id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isEmpty()) {
            throw new DataNotFoundException("해당 퀴즈는 존재하지 않습니다.");
        }
        return Quiz.entityToDto(quiz.get());
    }

    public QuizResponseDto getQuizResponseDtoByQuizNumber(Long id) {
        Optional<Quiz> quiz = quizRepository.findByQuizNumber(id);
        if (quiz.isEmpty()) {
            throw new DataNotFoundException("해당 퀴즈는 존재하지 않습니다.");
        }
        return Quiz.entityToDto(quiz.get());
    }

    public QuizRequestDto getQuizRequestDto(Long id) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isEmpty()) {
            throw new DataNotFoundException("해당 퀴즈는 존재하지 않습니다.");
        }
        return Quiz.entityToRequestDto(quiz.get());
    }

    public void patchQuiz(Long id, QuizRequestDto quizRequestDto) {
        Optional<Quiz> quiz = quizRepository.findById(id);
        if (quiz.isEmpty()) {
            throw new DataNotFoundException("해당 퀴즈는 존재하지 않습니다.");
        }
        quiz.get().setTitle(quizRequestDto.getTitle());
        quiz.get().setPythonContent(quizRequestDto.getPythonContent());
        quiz.get().setJavaContent(quizRequestDto.getJavaContent());
        quiz.get().setFirst(quizRequestDto.getFirst());
        quiz.get().setSecond(quizRequestDto.getSecond());
        quiz.get().setThird(quizRequestDto.getThird());
        quiz.get().setFourth(quizRequestDto.getFourth());
        quiz.get().setQuizAnswer(quizRequestDto.getQuizAnswer());
    }
}
