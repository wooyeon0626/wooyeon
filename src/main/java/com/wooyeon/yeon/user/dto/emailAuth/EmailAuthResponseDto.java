package com.wooyeon.yeon.user.dto.emailAuth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmailAuthResponseDto {
    //    private String statusCode;
    private String emailAuth;
    private String email;

    @Builder
    public EmailAuthResponseDto(String emailAuth, String email) {
        this.emailAuth = emailAuth;
        this.email = email;
    }

}
