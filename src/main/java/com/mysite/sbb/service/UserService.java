package com.mysite.sbb.service;

import com.mysite.sbb.exception.DataNotFoundException;
import com.mysite.sbb.entity.SiteUser;
import com.mysite.sbb.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SiteUser create(String username, String email, String password) {
        SiteUser user = new SiteUser();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return user;
    }

    public SiteUser getUser(String username, Boolean bool) {
        List<SiteUser> siteUserList = userRepository.findByUsernameAndIsOauth(username, bool);
        if (siteUserList.size() > 0) {
            return siteUserList.get(0);
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
}
