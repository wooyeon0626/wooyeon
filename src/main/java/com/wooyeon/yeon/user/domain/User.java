package com.wooyeon.yeon.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
//(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Column(length = 100)
    private String email;

    @Column
    private String password;

    @Column(length = 11)
    private String phone;

    @Column(unique = true, columnDefinition = "BINARY(16)")
    private UUID userCode;

    private String accessToken;

    private String refreshToken;

    @Column()
    private boolean emailAuth = false;
    private boolean phoneAuth = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROFILE_ID")
//    @Column(unique = true)
    private Profile profile;

    @Builder
    public User(String email, String phone, UUID userCode, String accessToken, String refreshToken, boolean emailAuth, boolean phoneAuth) {
        this.email = email;
        this.phone = phone;
        this.userCode = userCode;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.emailAuth = emailAuth;
        this.phoneAuth = phoneAuth;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void updateEmailAuth(boolean emailAuth) { this.emailAuth = emailAuth; }
    public void updatePhoneAuth(boolean phoneAuth) { this.phoneAuth = phoneAuth; }

}