package com.quiz.repository;

import com.quiz.entity.Quiz;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findAll();
    Optional<Quiz> findById(Long id);
    Optional<Quiz> findByQuizNumber(Long id);
}
