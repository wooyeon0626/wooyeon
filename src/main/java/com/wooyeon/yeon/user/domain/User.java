package com.wooyeon.yeon.user.domain;

import lombok.*;
import javax.persistence.*;

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

    @Column(length = 11)
    private String phone;

    private String accessToken;

    private String refreshToken;

    @Builder
    public User(Long userId, String email, String phone, String accessToken, String refreshToken) {
        this.userId=userId;
        this.email=email;
        this.phone=phone;
        this.accessToken=accessToken;
        this.refreshToken=refreshToken;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken=refreshToken;
    }
    public void updateAccessToken(String accessToken) {
        this.accessToken=accessToken;
    }


}