package com.mysite.sbb.entity;

import com.mysite.sbb.constant.UserRole;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private Boolean isOauth = false;
    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER;
}
