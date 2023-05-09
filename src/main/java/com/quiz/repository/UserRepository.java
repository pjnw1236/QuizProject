package com.quiz.repository;

import com.quiz.entity.SiteUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
    List<SiteUser> findAllByUsername(String username);
    List<SiteUser> findAllByEmail(String email);
    List<SiteUser> findByUsernameAndIsOauth(String username, Boolean bool);
    List<SiteUser> findByEmailAndIsOauth(String email, Boolean bool);
}
