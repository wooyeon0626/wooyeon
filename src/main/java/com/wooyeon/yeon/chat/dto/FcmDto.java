package com.wooyeon.yeon.chat.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;

public class FcmDto {

    @Getter
    @Builder
    public static class Request {
        @NotNull
        private String fcmToken;
    }

    @Getter
    @Builder
    public static class Response {
        private HttpStatus status;
    }
}
