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
    private String phone; // 휴대폰 번호

    @Column(length = 6, nullable = false)
    private String verifyCode; // 인증코드 6자리(난수)

    @Column(nullable = false)
    private LocalDateTime expireDate; // 인증코드 만료일

    private boolean certification; // 인증여부

    @Builder
    public PhoneAuth(String phone, String verifyCode, LocalDateTime expireDate, boolean certification) {
        this.phone = phone;
        this.verifyCode = verifyCode;
        this.expireDate = expireDate;
        this.certification = certification;
    }

    // 휴대폰 인증 완료
    public void phoneVerifiedSuccess() {
        this.certification = true;
    }

}