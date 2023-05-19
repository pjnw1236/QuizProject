package com.quiz.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizResponseDto {
    private Long id;
    private String title;
    private String pythonContent;
    private String javaContent;
    private String first;
    private String second;
    private String third;
    private String fourth;
    private Long quizNumber;
    private int quizAnswer;
}
