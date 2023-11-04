package com.wooyeon.yeon.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class LoginDto {

    @Getter
    public static class LoginRequest {
        private String userEmail;
        private String userPassword;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(userEmail, userPassword);
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class LoginResponse {
        private String accessToken;
        private String refreshToken;
    }


}
