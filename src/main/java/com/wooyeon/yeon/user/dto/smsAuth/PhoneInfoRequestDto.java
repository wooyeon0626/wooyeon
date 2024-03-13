package com.wooyeon.yeon.user.dto.smsAuth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PhoneInfoRequestDto {
    private String to;
    // 문자 자동 읽어오기에 필요한 앱 시그니처
    private String signature;
}