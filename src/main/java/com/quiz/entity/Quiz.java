package com.quiz.entity;

import com.quiz.dto.QuizRequestDto;
import com.quiz.dto.QuizResponseDto;
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

    private String commonContent;
    private String pythonContent;
    private String javaContent;
    private String javaScriptContent;

    private String first;
    private String second;
    private String third;
    private String fourth;
    private String fifth;

    private int quizNumber;
    private int quizAnswer;

    // QuizRequestDto -> Quiz 변환 메소드
    public static Quiz dtoToEntity(QuizRequestDto dto) {
        Quiz quiz = new Quiz();
        // 제목
        quiz.setTitle(dto.getTitle());
        // 내용
        quiz.setCommonContent(dto.getCommonContent());
        quiz.setPythonContent(dto.getPythonContent());
        quiz.setJavaContent(dto.getJavaContent());
        quiz.setJavaScriptContent(dto.getJavaScriptContent());
        // 오지선다
        quiz.setFirst(dto.getFirst());
        quiz.setSecond(dto.getSecond());
        quiz.setThird(dto.getThird());
        quiz.setFourth(dto.getFourth());
        quiz.setFifth(dto.getFifth());
        // 퀴즈 번호 및 답
        quiz.setQuizNumber(dto.getQuizNumber());
        quiz.setQuizAnswer(dto.getQuizAnswer());

        return quiz;
    }

    // Quiz -> QuizResponseDto 변환 메소드
    public static QuizResponseDto entityToDto(Quiz quiz) {
        QuizResponseDto dto = new QuizResponseDto();
        // 제목
        dto.setTitle(quiz.getTitle());
        // 내용
        dto.setCommonContent(quiz.getCommonContent());
        dto.setPythonContent(quiz.getPythonContent());
        dto.setJavaContent(quiz.getJavaContent());
        dto.setJavaScriptContent(quiz.getJavaScriptContent());
        // 오지선다
        dto.setFirst(quiz.getFirst());
        dto.setSecond(quiz.getSecond());
        dto.setThird(quiz.getThird());
        dto.setFourth(quiz.getFourth());
        dto.setFifth(quiz.getFifth());
        // 퀴즈 번호 및 답
        dto.setQuizNumber(quiz.quizNumber);
        dto.setQuizAnswer(quiz.quizAnswer);

        return dto;
    }
}
