package com.quiz.config.oauth;

import com.quiz.entity.Member;
import com.quiz.repository.UserRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("[userRequest.getClientRegistration().getClientId()] : " + userRequest.getClientRegistration().getClientId());
        log.info("[userRequest.getClientRegistration().getRegistrationId()] : " + userRequest.getClientRegistration().getRegistrationId());
        log.info("[userRequest.getClientRegistration().getClientName()] : " + userRequest.getClientRegistration().getClientName());
        log.info("[userRequest.getClientRegistration().toString()] : " + userRequest.getClientRegistration().toString());
        log.info("[userRequest.getClientRegistration().getClientSecret()] : " + userRequest.getClientRegistration().getClientSecret());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("[oAuth2User.getAttributes()] : " + oAuth2User.getAttributes());

//        String username = oAuth2User.getAttribute("name") + "_" + userRequest.getClientRegistration().getClientName();
        String username = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");
        Boolean bool = true;

        List<Member> memberList = userRepository.findByEmailAndIsOauth(email, bool);
        if (memberList.size() == 0) {
            Member member = new Member();
            member.setUsername(username);
            member.setEmail(email);
            member.setPassword("password");
            member.setIsOauth(true);
            userRepository.save(member);
        }

        return oAuth2User;
    }
}
