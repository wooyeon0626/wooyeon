package com.wooyeon.yeon.user.domain;

import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
//(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 20, nullable = false)
    private String password;

    @Column(length = 11)
    private String phone;

    private String refreshToken;
    private Boolean emailAuth;

    @Builder
    public User(Long userId, String email, String password, String phone, String refreshToken, Boolean emailAuth) {
        this.userId=userId;
        this.email=email;
        this.password=password;
        this.phone=phone;
        this.refreshToken=refreshToken;
        this.emailAuth=emailAuth;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken=refreshToken;
    }

    public void emailVerifiedSuccess() {
        this.emailAuth = true;
    }
}