package com.mysite.sbb.config;

import com.mysite.sbb.entity.SiteUser;
import com.mysite.sbb.repository.UserRepository;
import com.mysite.sbb.constant.UserRole;
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
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Boolean bool = false;
        List<SiteUser> siteUserList = userRepository.findByUsernameAndIsOauth(username, bool);

        if (siteUserList.size() == 0) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        SiteUser siteUser = siteUserList.get(0);
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (username.equals("admin")) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }

        return new User(siteUser.getUsername(), siteUser.getPassword(), authorities);
    }
}
