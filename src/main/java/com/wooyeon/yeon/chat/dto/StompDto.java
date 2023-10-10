package com.wooyeon.yeon.chat.dto;

import lombok.*;

@Getter
@Setter
public class StompDto {
    private Long roomId;
    private String sender;
    private String message;
}