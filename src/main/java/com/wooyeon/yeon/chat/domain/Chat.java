package com.wooyeon.yeon.chat.domain;

import com.wooyeon.yeon.profileChoice.domain.UserMatch;
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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userMatchId")
    private UserMatch userMatch;

    @Column
    private String sender;

    @Column(length = 2000)
    private String message;

    @Column
    private Timestamp sendTime;

    @Builder
    public Chat(Long chatId, UserMatch userMatch, String sender, String message, Timestamp sendTime) {
        this.chatId = chatId;
        this.userMatch = userMatch;
        this.sender = sender;
        this.message = message;
        this.sendTime = sendTime;
    }
}