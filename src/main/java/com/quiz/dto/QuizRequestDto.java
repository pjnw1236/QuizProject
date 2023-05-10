package com.quiz.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizRequestDto {
    @NotEmpty
    private String title;

    private String commonContent;
    private String pythonContent;
    private String javaContent;
    private String javaScriptContent;

    @NotEmpty
    private String first;
    @NotEmpty
    private String second;
    @NotEmpty
    private String third;
    @NotEmpty
    private String fourth;
    @NotEmpty
    private String fifth;

    @NotEmpty
    private int quizNumber;
    @NotEmpty
    private int quizAnswer;
}
