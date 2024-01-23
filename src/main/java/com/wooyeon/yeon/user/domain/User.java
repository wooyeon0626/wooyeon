package com.wooyeon.yeon.user.domain;

import lombok.AccessLevel;
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
@AllArgsConstructor
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

    @Column()
    private boolean emailAuth = false;
    private boolean phoneAuth = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROFILE_ID")
//    @Column(unique = true)
    private Profile profile;

    @Column
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

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
    public Long getUserId() {return this.userId;}
    public UUID getUserCode() {return this.userCode;}
    public String getRefreshToken() {return this.refreshToken;}

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
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