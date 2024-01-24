package com.wooyeon.yeon.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
public class PasswordEncryptRequestDto {
    private String email;
    private String encryptPassword;
    private String key;
}
