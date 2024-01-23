package com.wooyeon.yeon.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmailResponseDto {
    private String email;
    private int statusCode;
    private String statusName;

    @Builder
    public EmailResponseDto(String email, int statusCode, String statusName) {
        this.email = email;
        this.statusCode = statusCode;
        this.statusName = statusName;
    }

    public void updateStatusName(String statusName) {
        this.statusName = statusName;
    }

}
