package com.wooyeon.yeon.chat.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ChatDto {
    private int chatId;
    private int matchId;
    private int sender;
    private String message;
    private Timestamp sendTime;
}