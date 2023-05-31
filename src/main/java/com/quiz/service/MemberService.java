package com.quiz.service;

import com.quiz.constant.UserRole;
import com.quiz.exception.DataNotFoundException;
import com.quiz.entity.Member;
import com.quiz.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member create(String username, String email, String password) {
        Member user = new Member();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        if (username.equals("admin")) {
            user.setUserRole(UserRole.ADMIN);
        }
        memberRepository.save(user);
        return user;
    }

    public Member getUser(String username, Boolean bool) {
        List<Member> memberList = memberRepository.findByUsernameAndIsOauth(username, bool);
        if (memberList.size() > 0) {
            return memberList.get(0);
        } else {
            throw new DataNotFoundException("siteuser not found");
        }
    }
}
