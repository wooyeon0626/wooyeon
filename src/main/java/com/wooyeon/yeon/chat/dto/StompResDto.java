package com.wooyeon.yeon.chat.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class StompResDto {
    private String message;
    private String senderToken;
    private LocalDateTime sendTime;
}