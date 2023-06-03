package com.quiz.constant;

import com.quiz.entity.Member;
import com.quiz.repository.MemberRepository;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

public class AuthenticationUtil {
    public static Member getMember(MemberRepository memberRepository) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
            List<Member> memberList = memberRepository.findByUsernameAndIsOauth(auth.getName(), false);
            if (memberList.size() > 0) {
                return memberList.get(0);
            }
        } else if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            List<Member> memberList = memberRepository.findByEmailAndIsOauth(oauthToken.getPrincipal().getAttribute("email"), true);
            if (memberList.size() > 0) {
                return memberList.get(0);
            }
        }
        return null;
    }
}
