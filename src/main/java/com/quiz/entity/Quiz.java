package com.quiz.entity;

import com.quiz.dto.QuizRequestDto;
import com.quiz.dto.QuizResponseDto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(length = 2000)
    private String pythonContent;
    @Column(length = 2000)
    private String javaContent;

    private String first;
    private String second;
    private String third;
    private String fourth;

    private Long quizNumber;
    private int quizAnswer;

    // QuizRequestDto -> Quiz 변환 메소드
    public static Quiz dtoToEntity(QuizRequestDto dto) {
        Quiz quiz = new Quiz();
        // 제목
        quiz.setTitle(dto.getTitle());
        // 내용
        quiz.setPythonContent(dto.getPythonContent());
        quiz.setJavaContent(dto.getJavaContent());
        // 오지선다
        quiz.setFirst(dto.getFirst());
        quiz.setSecond(dto.getSecond());
        quiz.setThird(dto.getThird());
        quiz.setFourth(dto.getFourth());
        // 퀴즈 번호 및 답
        quiz.setQuizNumber(dto.getQuizNumber());
        quiz.setQuizAnswer(dto.getQuizAnswer());

        return quiz;
    }

    // Quiz -> QuizResponseDto 변환 메소드
    public static QuizResponseDto entityToDto(Quiz quiz) {
        QuizResponseDto dto = new QuizResponseDto();

        dto.setId(quiz.getId());
        // 제목
        dto.setTitle(quiz.getTitle());
        // 내용
        dto.setPythonContent(quiz.getPythonContent());
        dto.setJavaContent(quiz.getJavaContent());
        // 오지선다
        dto.setFirst(quiz.getFirst());
        dto.setSecond(quiz.getSecond());
        dto.setThird(quiz.getThird());
        dto.setFourth(quiz.getFourth());
        // 퀴즈 번호 및 답
        dto.setQuizNumber(quiz.quizNumber);
        dto.setQuizAnswer(quiz.quizAnswer);

        return dto;
    }

    // QuizList -> QuizResponseDtoList 변환 메소드
    public static List<QuizResponseDto> entityListToDtoList(List<Quiz> quizList) {
        List<QuizResponseDto> quizResponseDtoList = new ArrayList<>();
        for (Quiz quiz : quizList) {
            QuizResponseDto quizResponseDto = Quiz.entityToDto(quiz);
            quizResponseDtoList.add(quizResponseDto);
        }
        return quizResponseDtoList;
    }

    // Quiz -> QuizRequestDto 변환 메소드
    public static QuizRequestDto entityToRequestDto(Quiz quiz) {
        QuizRequestDto quizRequestDto = new QuizRequestDto();

        quizRequestDto.setTitle(quiz.getTitle());

        quizRequestDto.setPythonContent(quiz.getPythonContent());
        quizRequestDto.setJavaContent(quiz.getJavaContent());

        quizRequestDto.setFirst(quiz.getFirst());
        quizRequestDto.setSecond(quiz.getSecond());
        quizRequestDto.setThird(quiz.getThird());
        quizRequestDto.setFourth(quiz.getFourth());

        quizRequestDto.setQuizNumber(quiz.quizNumber);
        quizRequestDto.setQuizAnswer(quiz.quizAnswer);

        return quizRequestDto;
    }
}
