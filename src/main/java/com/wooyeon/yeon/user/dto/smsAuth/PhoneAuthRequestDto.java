package com.wooyeon.yeon.user.dto.smsAuth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class PhoneAuthRequestDto {
    private String phone;
    private String verifyCode;
}
