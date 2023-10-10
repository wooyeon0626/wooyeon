package com.wooyeon.yeon.chat.dto;

import lombok.Builder;
import lombok.Getter;

public class ChatUserDto {

    @Getter
    public static class ChatUserRequest {
        private Long matchUserId;
    }
}