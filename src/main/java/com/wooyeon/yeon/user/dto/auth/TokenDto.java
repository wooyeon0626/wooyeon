package com.wooyeon.yeon.user.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TokenDto {
    private String accessToken;
    private String refreshToken;
    private int statusCode;

    public void updateStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}