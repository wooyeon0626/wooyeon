package com.wooyeon.yeon.user.dto;

import lombok.Builder;
import lombok.Getter;

public class ExpiredCheckDto {
    @Getter
    @Builder
    public static class ExpiredCheckRequest {
        private String accessToken;
        private String refreshToken;
    }

    @Builder
    @Getter
    public static class ExpiredCheckResponse {
        private int existsProfile;
    }
}
