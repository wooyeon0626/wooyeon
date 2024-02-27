package com.wooyeon.yeon.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

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

    private String targetToken;
    private String fcmToken;

    @Column
    @Builder.Default
    private boolean emailAuth = false;

    @Builder.Default
    private boolean phoneAuth = false;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private Profile profile;

    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserRoles userRoles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Builder
    public User(String email, String phone, UUID userCode, String accessToken, String refreshToken, boolean emailAuth, boolean phoneAuth, String password, String fcmToken) {
        this.email = email;
        this.phone = phone;
        this.userCode = userCode;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.emailAuth = emailAuth;
        this.phoneAuth = phoneAuth;
        this.password = password;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void updatePassword(String password) { this.password = password; }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void updateEmailAuth(boolean emailAuth) { this.emailAuth = emailAuth; }
    public void updatePhoneAuth(boolean phoneAuth) { this.phoneAuth = phoneAuth; }
    public Long getUserId() {return this.userId;}
    public UUID getUserCode() {return this.userCode;}
    public String getRefreshToken() {return this.refreshToken;}
    public String getAccessToken() {return this.accessToken;}
    public String getFcmToken() {return this.fcmToken;}
    public String getUserEmail() {return this.email;}
    public Profile getUserProfile() {return this.profile;}

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}