package com.quiz.dto;

import com.quiz.constant.QuizType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberQuizResultDto {
    private String title;
    private String pythonContent;
    private String javaContent;
    private String first;
    private String second;
    private String third;
    private String fourth;

    private int quizNumber;
    private int quizAnswer;

    private Integer memberQuizAnswer;
    private QuizType quizType;

    private boolean isRight = false;
}
