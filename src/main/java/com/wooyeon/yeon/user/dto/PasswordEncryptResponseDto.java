package com.wooyeon.yeon.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
public class PasswordEncryptResponseDto {
    private int statusCode;
    private String statusName;
}
