package com.wooyeon.yeon.user.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class SmsAuthResponseDto {
    private String requestId;
    private LocalDateTime requestTime;
    private String statusCode;
    private String statusName;
}
