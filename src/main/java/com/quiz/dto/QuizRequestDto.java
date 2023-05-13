package com.quiz.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
public class QuizRequestDto {
    @NotEmpty(message = "Quiz 제목은 필수입니다.")
    private String title;

    @NotEmpty(message = "Python 코드 입력은 필수입니다.")
    @Size(max = 2000, message = "Python 코드 입력은 2000자 이하여야 합니다.")
    private String pythonContent;
    @NotEmpty(message = "Java 코드 입력은 필수입니다.")
    @Size(max = 2000, message = "Java 코드 입력은 2000자 이하여야 합니다.")
    private String javaContent;
    @NotEmpty(message = "JavaScript 코드 입력은 필수입니다.")
    @Size(max = 2000, message = "JavaScript 코드 입력은 2000자 이하여야 합니다.")
    private String javaScriptContent;

    @NotEmpty(message = "1번 보기는 필수입니다.")
    private String first;
    @NotEmpty(message = "2번 보기는 필수입니다.")
    private String second;
    @NotEmpty(message = "3번 보기는 필수입니다.")
    private String third;
    @NotEmpty(message = "4번 보기는 필수입니다.")
    private String fourth;
    @NotEmpty(message = "5번 보기는 필수입니다.")
    private String fifth;

    @Range(min=1, max=10, message="Quiz 번호는 1에서 10 사이입니다.")
    private Integer quizNumber;
    @Range(min=1, max=5, message="Quiz 정답은 1번에서 5번 사이입니다.")
    private Integer quizAnswer;
}
