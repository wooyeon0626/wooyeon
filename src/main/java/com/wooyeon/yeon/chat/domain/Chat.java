package com.wooyeon.yeon.chat.domain;

import com.wooyeon.yeon.profileChoice.domain.Match;
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

    @OneToOne
    @JoinColumn(name = "") // 외래키 이름 미정
    private Match match;

    @Column
    private String sender;

    @Column(length = 2000)
    private String message;

    @Column
    private Timestamp sendTime;

    @Builder
    public Chat(Long chatId, Match match, String sender, String message, Timestamp sendTime) {
        this.chatId = chatId;
        this.match = match;
        this.sender = sender;
        this.message = message;
        this.sendTime = sendTime;
    }
}