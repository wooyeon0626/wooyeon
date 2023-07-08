package com.wooyeon.yeon.chat.domain;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long chatId;
    @Column
    private Long matchId;
    @Column
    private String sender;
    @Column(length = 2000)
    private String message;
    @Column
    private Timestamp sendTime;

    @Builder
    public Chat(Long chatId, Long matchId, String sender, String message, Timestamp sendTime) {
        this.chatId = chatId;
        this.matchId = matchId;
        this.sender = sender;
        this.message = message;
        this.sendTime = sendTime;
    }
}