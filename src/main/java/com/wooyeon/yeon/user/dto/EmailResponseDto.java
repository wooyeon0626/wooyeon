package com.wooyeon.yeon.user.dto;

import lombok.Builder;

public class EmailResponseDto {
//  private String statusCode;
    private String email;
    private String authToken;

    @Builder
    public EmailResponseDto(String email, String authToken) {
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

}
