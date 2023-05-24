package com.quiz.dto;

import com.quiz.constant.QuizType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberQuizDto {
    private int quizNumber;
    private int quizAnswer;
    private int memberQuizAnswer;
    private QuizType quizType;
}
