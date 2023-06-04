package com.quiz.config;

import com.quiz.config.oauth.OAuth2UserService;
import com.quiz.constant.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final OAuth2UserService oAuth2UserService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .mvcMatchers("/admin/**").hasAuthority(UserRole.ADMIN.getValue())
            .mvcMatchers("/quiz/**").hasAnyAuthority(UserRole.ADMIN.getValue(), UserRole.USER.getValue())

            // 게시글 등록
            .mvcMatchers(HttpMethod.GET, "/question/register").hasAnyAuthority(UserRole.ADMIN.getValue(), UserRole.USER.getValue())
            .mvcMatchers(HttpMethod.POST, "/question").hasAnyAuthority(UserRole.ADMIN.getValue(), UserRole.USER.getValue())

            // 게시글 수정
            .mvcMatchers(HttpMethod.GET, "/question/edit/{id:\\d+}").hasAnyAuthority(UserRole.ADMIN.getValue(), UserRole.USER.getValue())
            .mvcMatchers(HttpMethod.PATCH, "/question/{id:\\d+}").hasAnyAuthority(UserRole.ADMIN.getValue(), UserRole.USER.getValue())

            // 게시글 삭제
            .mvcMatchers(HttpMethod.DELETE, "/question/{id:\\d+}").hasAnyAuthority(UserRole.ADMIN.getValue(), UserRole.USER.getValue())

            .anyRequest().permitAll()
                .and()
            .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
            .formLogin().loginPage("/member/login").defaultSuccessUrl("/")
                .and()
            .logout().logoutRequestMatcher(new AntPathRequestMatcher("/member/logout")).logoutSuccessUrl("/").invalidateHttpSession(true)
                .and()
            .oauth2Login().loginPage("/member/login").userInfoEndpoint().userService(oAuth2UserService);
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
