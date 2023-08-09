package com.wooyeon.yeon.chat.dto;

import lombok.*;

@Getter
@Setter
public class ChatDto {
    public enum MessageType {
        ENTER, COMM
    }

    private MessageType type;
    private Long roomId;
    private String sender;
    private String message;
}