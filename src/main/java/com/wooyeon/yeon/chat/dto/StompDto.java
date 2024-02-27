package com.wooyeon.yeon.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
public class StompDto {
    private Long roomId;
    private String message;
    private String type;

    public enum MessageType {
        ENTER, QUIT, TALK
    }

    @Builder
    public static class StompRes {
        private String message;
        private String senderToken;
        private LocalDateTime sendTime;
    }
}