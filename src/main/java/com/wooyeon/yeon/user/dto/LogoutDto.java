package com.wooyeon.yeon.user.dto;

import lombok.*;

public class LogoutDto {

    @Getter
    @NoArgsConstructor
    public static class LogoutRequest {
        private String accessToken;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LogoutResponse {
        private String status;
    }
}
