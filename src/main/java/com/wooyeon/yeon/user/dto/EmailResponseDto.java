package com.wooyeon.yeon.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmailResponseDto {
    //  private String statusCode;
    private String email;
    private String statusName;

    @Builder
    public EmailResponseDto(String email, String statusName) {
        this.email = email;
        this.statusName = statusName;
    }

}
