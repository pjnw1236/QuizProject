package com.quiz.config;

import com.quiz.entity.Member;
import com.quiz.repository.MemberRepository;
import com.quiz.constant.UserRole;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Boolean bool = false;
        List<Member> memberList = memberRepository.findByUsernameAndIsOauth(username, bool);

        if (memberList.size() == 0) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        Member member = memberList.get(0);
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (username.equals("admin")) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }

        return new User(member.getUsername(), member.getPassword(), authorities);
    }
}
