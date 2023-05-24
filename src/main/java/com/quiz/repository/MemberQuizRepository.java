package com.quiz.repository;

import com.quiz.entity.MemberQuiz;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberQuizRepository extends JpaRepository<MemberQuiz, Long> {
    Optional<MemberQuiz> findByQuizNumber(int id);
}
