package com.wooyeon.yeon.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class PasswordEncryptRequestDto {
    private String email;
    private String encryptedPassword;
    private String encryptedKey;

    public String getEmail() { return this.email=email; }
    public String getEncryptedPassword() { return this.encryptedPassword = encryptedPassword; }
    public String getEncryptedKey() { return this.encryptedKey = encryptedKey; }

}
