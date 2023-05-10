package com.quiz.entity.quiz;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AdminQuiz {
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
}
