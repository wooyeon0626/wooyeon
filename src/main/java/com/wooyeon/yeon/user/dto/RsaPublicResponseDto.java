package com.wooyeon.yeon.user.dto;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
public class RsaPublicResponseDto {
    private String publicKey;
}
