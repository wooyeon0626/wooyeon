package com.wooyeon.yeon.user.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class LoginDto {

    @Getter
    public static class LoginRequest {
        private String email;
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }
        public void updateEmail(String email) { this.email = email; }
        public void updatePassword(String password) { this.password = password; }
    }
}
