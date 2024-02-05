package com.wooyeon.yeon.chat.dto;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class ChatDto {

    @Getter
    @Builder
    public static class Request {
        @NotNull
        private Long matchId;
    }

    @Getter
    @Builder
    public static class Response {
        private String message;
        private LocalDateTime sendTime;
        private String sender;
        private Boolean isChecked;
        private Boolean isSender;
    }

}
