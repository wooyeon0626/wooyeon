package com.wooyeon.yeon.chat.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "chat")
@Getter
@RequiredArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int chatId;
    private int matchId;
    private String sender;
    private String message;
    private Timestamp sendTime;

    @Builder
    public Chat(int chatId, int matchId, String sender, String message) {
        this.chatId = chatId;
        this.matchId = matchId;
        this.sender = sender;
        this.message = message;
    }
}