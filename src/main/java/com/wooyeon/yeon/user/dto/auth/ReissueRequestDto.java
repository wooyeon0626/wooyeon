package com.wooyeon.yeon.user.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReissueRequestDto {
    private String accessToken;
    private String refreshToken;
}
