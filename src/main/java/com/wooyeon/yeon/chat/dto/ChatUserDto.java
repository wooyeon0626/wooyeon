package com.wooyeon.yeon.chat.dto;

import lombok.Getter;

public class ChatUserDto {

    @Getter
    public static class ChatUserRequest {
        private Long userId;
        private Long matchUserId;
    }
}