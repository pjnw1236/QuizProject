package com.quiz.service;

import com.quiz.dto.QuizRequestDto;
import com.quiz.dto.QuizResponseDto;
import com.quiz.entity.Quiz;
import com.quiz.exception.DataNotFoundException;
import com.quiz.repository.AdminRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public QuizResponseDto createQuiz(QuizRequestDto quizRequestDto) {
        Quiz quiz = Quiz.dtoToEntity(quizRequestDto);
        adminRepository.save(quiz);
        QuizResponseDto quizResponseDto = Quiz.entityToDto(quiz);
        return quizResponseDto;
    }

    public List<QuizResponseDto> getQuizList() {
        List<Quiz> quizList = adminRepository.findAll();
        List<QuizResponseDto> quizResponseDtoList = Quiz.entityListToDtoList(quizList);
        return quizResponseDtoList;
    }

    public QuizResponseDto getQuiz(Long id) {
        Quiz quiz = adminRepository.findById(id);
        if (quiz == null) {
            throw new DataNotFoundException("해당 퀴즈는 존재하지 않습니다.");
        }
        QuizResponseDto quizResponseDto = Quiz.entityToDto(quiz);
        return quizResponseDto;
    }
}
