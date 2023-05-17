package com.quiz.repository;

import com.quiz.entity.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByUsername(String username);
    List<Member> findAllByEmail(String email);
    List<Member> findByUsernameAndIsOauth(String username, Boolean bool);
    List<Member> findByEmailAndIsOauth(String email, Boolean bool);
}
