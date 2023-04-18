package com.mysite.sbb.config.oauth;

import com.mysite.sbb.entity.SiteUser;
import com.mysite.sbb.repository.UserRepository;
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

        List<SiteUser> siteUserList = userRepository.findAllByOauthEmail(email);
        if (siteUserList.size() == 0) {
            SiteUser siteUser = new SiteUser();
            siteUser.setUsername(username);
            siteUser.setEmail(email);
            siteUser.setPassword("password");
            siteUser.setIsOauth(true);
            userRepository.save(siteUser);
        }

        return oAuth2User;
    }
}