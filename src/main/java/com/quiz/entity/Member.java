package com.quiz.entity;

import com.quiz.constant.UserRole;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private Boolean isOauth = false;
    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MemberQuiz> memberQuizList = new ArrayList<>();

    public void addMemberQuiz(MemberQuiz memberQuiz) {
        this.memberQuizList.add(memberQuiz);
    }

    public MemberQuiz getMemberQuizByQuizNumber(Long quizNumber) {
        for (MemberQuiz memberQuiz : memberQuizList) {
            if (memberQuiz.getQuizNumber() == quizNumber) {
                return memberQuiz;
            }
        }
        return null;
    }
}
