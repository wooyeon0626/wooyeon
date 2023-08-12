package com.wooyeon.yeon.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PhoneAuthResponseDto {
//    private String statusCode;
    private String phoneAuth;
    private String register;
    private String profile;
    private String serviceTerms;
}
