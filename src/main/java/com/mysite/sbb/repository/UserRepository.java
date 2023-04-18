package com.mysite.sbb.repository;

import com.mysite.sbb.entity.SiteUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
//    Optional<SiteUser> findByusername(String username);
//    List<SiteUser> findAllByOauthEmail(String email);
    List<SiteUser> findAllByUsername(String username);
    List<SiteUser> findAllByEmail(String email);
    List<SiteUser> findByUsernameAndIsOauth(String username, Boolean bool);
    List<SiteUser> findByEmailAndIsOauth(String email, Boolean bool);
}
