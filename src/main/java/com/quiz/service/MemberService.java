package com.quiz.service;

import com.quiz.constant.UserRole;
import com.quiz.exception.DataNotFoundException;
import com.quiz.entity.Member;
import com.quiz.form.MemberRegisterForm;
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

    public Member create(MemberRegisterForm memberRegisterForm) {
        Member member = new Member();
        member.setUsername(memberRegisterForm.getUsername());
        member.setEmail(memberRegisterForm.getEmail());
        member.setPassword(passwordEncoder.encode(memberRegisterForm.getPassword1()));
        if (member.getUsername().equals("admin")) {
            member.setUserRole(UserRole.ADMIN);
        }
        memberRepository.save(member);
        return member;
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
