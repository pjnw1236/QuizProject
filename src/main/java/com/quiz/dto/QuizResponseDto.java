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
//    private String javaScriptContent;
    private String first;
    private String second;
    private String third;
    private String fourth;
    private String fifth;
    private int quizNumber;
    private int quizAnswer;
}
