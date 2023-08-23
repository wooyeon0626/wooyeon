package com.wooyeon.yeon.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmailAuthResponseDto {
//    private String statusCode;
    private String emailAuth;

    @Builder
    public EmailAuthResponseDto(String emailAuth) {
        this.emailAuth=emailAuth;
    }

}
