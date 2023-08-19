package com.wooyeon.yeon.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 11, nullable = false)
    private String phone;

    @Column(length = 6, nullable = false)
    private String verifyCode;

    @Column(nullable = false)
    private LocalDateTime expireDate;

    private boolean expired;
    private boolean certification;

    @Builder
    public PhoneAuth(String phone, String verifyCode, LocalDateTime expireDate, boolean expired, boolean certification) {
        this.phone = phone;
        this.verifyCode = verifyCode;
        this.expireDate = expireDate;
        this.expired = expired;
        this.certification = certification;
    }

    // 휴대폰 인증 완료
    public void phoneVerifiedSuccess() {
        this.certification = true;
    }

    // 휴대폰 인증 코드 만료
    public void ExpiredVerifyCode() {
        this.expired = true;
    }
}
