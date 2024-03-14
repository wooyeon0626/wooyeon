package com.wooyeon.yeon.user.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
public class LoginRequestDto {

    private String email;
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
    public void updateEmail(String email) { this.email = email; }
    public void updatePassword(String password) { this.password = password; }
}
