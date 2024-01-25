package com.wooyeon.yeon.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class PasswordEncryptRequestDto {
    private String email;
    private String encryptedPassword;
    private String encryptedKey;
}
