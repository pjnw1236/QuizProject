package com.quiz.repository;

import com.quiz.entity.AdminQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminQuiz, Integer> {

}
