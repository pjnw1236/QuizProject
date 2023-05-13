package com.quiz.repository;

import com.quiz.entity.Quiz;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Quiz, Integer> {
    List<Quiz> findAll();
    Quiz findById(Long id);
}
