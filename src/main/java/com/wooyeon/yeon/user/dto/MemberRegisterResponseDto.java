package com.wooyeon.yeon.user.dto;

import lombok.Builder;

public class MemberRegisterResponseDto {

    private String email;
    private String authToken;

    @Builder
    public MemberRegisterResponseDto(String email, String authToken) {
        this.email = email;
        this.authToken = authToken;
    }

    // 게터, 세터 생략 (Lombok을 사용하면 자동으로 생성)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
